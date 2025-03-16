<template>
  <div class="reconstruct-play-view">
    <div class="reconstruct-content">
      <AnimalDisplay :animals="animals" class="animal-display"/>
      <div class="board" :style="getBoardStyle()">
        <div v-for="coords in getCoords()" :key="coords" class="tile" :style="getTileStyle(coords)"
             :class="bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length > 0 ?
             'best-click' : ''" @click="clickedCoords(coords)" @contextmenu="rightClickedCoords($event)">

        </div>
      </div>
      <config-menu :style="!showConfig ? 'display: none;' : ''" class="config-menu dock-bottom" :animals="animals"
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
  max-width: min(90%, 400px);
  margin: auto;
}

.animal-display {
  position: absolute;
  width: 100%;
  top: 0;
}

.board {
  margin-top: 8rem;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 3px;
  padding: .4rem;
}

.tile {
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
  width: 100%;
  z-index: 1;
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
import {Game} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {getRegionColors} from "@/util/region-colors";
import {useSolver} from "@/store/useSolver";
import {getHeatmapColor} from "@/util/heatmap-colors";
import ConfigMenu from "@/views/ConfigMenu.vue";
import {sortAnimalsByRarity} from "@/util/animal-sorter";


const state = useState()
const gameStore = useGame()
const solver = useSolver()

export default defineComponent({
  name: "ReconstructPlayView",
  components: {ConfigMenu, MenuBar, AnimalDisplay},
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
      animalForHeatmap: null as Animal | null
    }
  },

  watch: {
    game: {
      async handler(game: Game | null) {
        if (game) {
          this.updateProbabilityInfo()
        }
      },
      immediate: true
    }
  },

  async created() {
    if (this.animals.length === 0) {
      await router.push({name: "reconstruct-region", params: {region: state.selectedRegion}})
      return
    }
    this.game = await gameStore.startReconstruct(this.animals)
    sortAnimalsByRarity(this.animals)
  },

  methods: {
    async updateProbabilityInfo() {
      if (!this.game || !this.animalForHeatmap) {
        return
      }
      const info = await solver.solve(this.game!, this.animalForHeatmap!)
      this.probabilities = info.probabilities
      this.bestClicks = info.bestClicks
      this.maxProb = Math.max(...info.probabilities.flat())
      this.minProb = Math.min(...info.probabilities.flat())
    },

    onBack() {
      router.push({name: "reconstruct-region", params: {region: state.selectedRegion}})
    },
    onConfig() {
      this.showConfig = !this.showConfig
    },

    onHeatMapSelectChange(animal: Animal) {
      this.animalForHeatmap = animal
      this.updateProbabilityInfo()
    },

    onPlaceSelectChange(animal: Animal) {
      this.animalToPlace = animal
    },

    async clickedCoords(coords: Coords) {
      this.game = await gameStore.clickReconstruct(this.game!, this.animalToPlace, coords)
    },

    async rightClickedCoords(event: MouseEvent, coords: Coords) {
      event.preventDefault()
      this.game = await gameStore.clickReconstruct(this.game!, null, coords)
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
          const animalRarity = tile.animalBoardInstance.animal.rarity
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
    }
  }
})
</script>