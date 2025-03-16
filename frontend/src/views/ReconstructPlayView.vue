<template>
  <div class="reconstruct-play-view">
    <div class="reconstruct-content">
      <AnimalDisplay :animals="animals" class="animal-display"/>
      <div class="board" :style="getBoardStyle()">
        <div v-for="coords in getCoords()" :key="coords" class="tile" :style="getTileStyle(coords)"
             :class="bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length > 0 ?
             'best-click' : ''">

        </div>
      </div>
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


const state = useState()
const gameStore = useGame()
const solver = useSolver()

export default defineComponent({
  name: "ReconstructPlayView",
  components: {MenuBar, AnimalDisplay},
  data() {
    return {
      animals: state.selectedAnimals as Animal[],
      game: null as Game | null,
      probabilities: null as number[][] | null,
      maxProb: null as number | null,
      minProb: null as number | null,
      bestClicks: [] as Coords[]
    }
  },

  watch: {
    game: {
      async handler(game: Game | null) {
        if (game) {
          const info = await solver.solve(game, this.animals[0])
          this.probabilities = info.probabilities
          this.bestClicks = info.bestClicks
          console.log(this.bestClicks)
          this.maxProb = Math.max(...info.probabilities.flat())
          this.minProb = Math.min(...info.probabilities.flat())
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
  },

  methods: {
    onBack() {
      router.push({name: "reconstruct-region", params: {region: state.selectedRegion}})
    },
    onConfig() {
      console.log("config")
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