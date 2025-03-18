<template>
  <div class="reconstruct-play-view" :style="getBackgroundStyle()">
    <div class="reconstruct-content">
      <AnimalDisplay :animals="animals" :tracker="animalTracker" class="animal-display"
                     @animal-click="onPlaceSelectChange" :animal-to-place="animalToPlace"/>
      <div class="board" :style="getBoardStyle()">
        <div v-for="coords in getCoords()" :key="coords" class="tile" :style="getTileStyle(coords)"
             :class="bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length > 0 ?
             'best-click' : ''" @click="clickedCoords(coords)" @contextmenu="rightClickedCoords($event, coords)">
          <AnimalSquare v-if="game && game.board[coords.x][coords.y].occupied && game.board[coords.x][coords.y].revealed"
                        :animal="game.board[coords.x][coords.y].animalBoardInstance.animal" class="animal-square"/>
<!--          <div v-else-if="game && probabilities">{{probabilities[coords.x][coords.y].toFixed(3)}}</div>-->
        </div>
      </div>
      <config-menu :style="!showConfig ? 'display: none;' : ''" class="config-menu dock-bottom dock-bottom-shadow" :animals="animals"
                   :heat-map-animal="animalForHeatmap" :place-animal="animalToPlace"
      @animal-heatmap-select="onHeatMapSelectChange" @animal-place-select="onPlaceSelectChange"/>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="onConfig" second-color-class="color-action-neutral-2"
              second-button-name="config"/>
  </div>
</template>

<style scoped>
.reconstruct-play-view {
  display: flex;
  flex-direction: column;
}

.reconstruct-content {
  position: relative;
  flex: 1;
  display: grid;
  place-items: center;
}

.animal-display {
  position: absolute;
  width: 100%;
  top: 0;
}

.board {
  max-width: min(90%, 400px);
  margin-inline: auto;
  margin-top: 3rem;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 3px;
  padding: 3px;
}

.tile {
  position: relative;
  display: grid;
  place-items: center;
  background-color: gray;
  aspect-ratio: 1;
  min-width: 4rem;
}

.best-click {
  border: white solid 2px;
}


.config-menu {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  max-width: min(70%, 400px);
  z-index: 1;
}

.animal-square {
  max-width: 100%;
  max-height: 100%;
  position: absolute;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import {useState} from "@/store/useState";
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import {useGame} from "@/store/useGame";
import {ClickChangeInfo, Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {getRegionColors} from "@/util/region-colors";
import {useSolver} from "@/store/useSolver";
import {getHeatmapColor} from "@/util/heatmap-colors";
import ConfigMenu from "@/views/ConfigMenu.vue";
import {sortAnimalsByRarity} from "@/util/animal-sorter";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";


const state = useState()
const gameStore = useGame()
const solver = useSolver()

export default defineComponent({
  name: "ReconstructPlayView",
  components: {AnimalSquare, ConfigMenu, MenuBar, AnimalDisplay},
  data() {
    return {
      animals: state.selectedAnimals as Animal[],
      game: null as Game | null,
      probabilities: null as number[][] | null,
      maxProb: null as number | null,
      minProb: null as number | null,
      bestClicks: [] as Coords[],
      showConfig: false,
      animalToPlace: null as Animal | null,
      animalForHeatmap: null as Animal | null,
      animalTracker: new Map<Animal, number>()
    }
  },

  watch: {
    game: {
      async handler(game: Game | null) {
        if (game) {
          this.updateProbabilityInfo()
          this.updateTracker()
        }
      },
      immediate: true
    }
  },

  async created() {
    if (this.animals.length === 0) {
      await router.push({name: "reconstruct-region", params: {region: state.selectedRegion?.toLowerCase()}})
      return
    }
    this.game = await gameStore.startReconstruct(this.animals)
    sortAnimalsByRarity(this.animals)

    this.animalForHeatmap = this.animals[this.animals.length - 1]
    this.animalToPlace = null
  },

  methods: {
    async updateProbabilityInfo() {
      if (!this.game || !this.animalForHeatmap) {
        this.probabilities = null;
        this.bestClicks = []
      }
      const info = await solver.solve(this.game!, this.animalForHeatmap!)
      this.probabilities = info.probabilities
      this.bestClicks = info.bestClicks
      if (!info.probabilities) {
        this.maxProb = 0
        this.minProb = 0
      } else {
        this.maxProb = Math.max(...info.probabilities.flat())
        this.minProb = Math.min(...info.probabilities.flat())
      }
    },

    updateTracker() {
      this.animalTracker = new Map()
      for (const animal of this.animals) {
        this.animalTracker.set(animal, this.game?.board.flat().filter(tile => tile.occupied &&
            tile.animalBoardInstance.animal.name === animal.name).length || 0)
      }
    },


    onBack() {
      router.push({name: "reconstruct-region", params: {region: state.selectedRegion?.toLowerCase()}})
    },
    onConfig() {
      this.showConfig = !this.showConfig
    },

    onHeatMapSelectChange(animal: Animal) {
      this.animalForHeatmap = animal
      this.updateProbabilityInfo()
    },

    onPlaceSelectChange(animal: Animal | null) {
      if (this.animalToPlace === animal) {
        animal = null
      }
      this.animalToPlace = animal
    },

    async clickedCoords(coords: Coords) {
      let clickInfo = await gameStore.clickReconstruct(this.game!, this.animalToPlace, coords)
      this.updateGame(clickInfo, coords)
    },

    async rightClickedCoords(event: MouseEvent, coords: Coords) {
      event.preventDefault()
      let clickInfo = await gameStore.clickReconstruct(this.game!, null, coords)
      this.updateGame(clickInfo, coords)
    },

    updateGame(clickInfo : ClickChangeInfo, coords: Coords) {
      if (!this.game)
        return
      if (clickInfo.wasValidClick) {
        this.game.board[coords.x][coords.y] = clickInfo.updatedTile

        if (this.game.notCompletelyRevealedAnimalsWithoutBux.length == 0 &&
            clickInfo.notCompletelyRevealedAnimalsWithoutBux.length != 0)
          this.animalForHeatmap = clickInfo.notCompletelyRevealedAnimalsWithoutBux[0]

        this.game.completelyRevealedAnimals = clickInfo.completelyRevealedAnimals
        this.game.notCompletelyRevealedAnimalsWithoutBux = clickInfo.notCompletelyRevealedAnimalsWithoutBux

        if (this.game.completelyRevealedAnimals.filter(animal => animal.name === this.animalForHeatmap?.name).length > 0) {
          if (this.game.notCompletelyRevealedAnimalsWithoutBux.length > 0)
            this.animalForHeatmap = this.game.notCompletelyRevealedAnimalsWithoutBux[0]
          else
            this.animalForHeatmap = null
        }
        if (this.game.completelyRevealedAnimals.filter(animal => animal.name === this.animalToPlace?.name).length > 0
            && this.game.notCompletelyRevealedAnimalsWithoutBux.length > 0)
          this.animalToPlace = null

        this.updateProbabilityInfo()
        this.updateTracker()

      }
    },

    getCoords() {
      if (!this.game) {
        return []
      }
      return this.game.board.flatMap((row, y) => row.map((animal, x) => ({x, y} as Coords)))
    },

    getTileStyle(coords: Coords) {
      const regionColors = state.selectedRegion ? getRegionColors(state.selectedRegion) : {
        dark: "black", light: "white", primary: "gray"
      }
      if (!this.game) {
        return {}
      }
      const tile = this.game.board[coords.x][coords.y]
      if (tile.revealed) {
        if (tile.occupied) {
          return {}
        } else {
          return {backgroundColor: "rgba(0,0,0,0.5)"}
        }
      }
      else {
        if (!this.probabilities) {
          return {backgroundColor: regionColors.primary}
        } else {
          const probability = this.probabilities[coords.x][coords.y]
          return {backgroundColor: getHeatmapColor(probability, this.minProb!, this.maxProb!)}
        }
      }
    },

    getBoardStyle() {
      if (!this.game) {
        return {}
      }
      const regionColors = state.selectedRegion ? getRegionColors(state.selectedRegion) : {
        dark: "black", light: "white", primary: "gray"
      }
      return {backgroundColor: regionColors.dark}
    },

    getBackgroundStyle() {
      if (!state.selectedRegion) {
        return {}
      }
      const regionColors = getRegionColors(state.selectedRegion)
      return {backgroundColor: regionColors.light}
    }
  }
})
</script>