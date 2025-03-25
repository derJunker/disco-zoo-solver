<template>
  <div class="accuracy-single-click-result-details-view">
    <div class="accuracy-single-click-result-details-content">
      <div class="wood-menu menu-bottom dock-bottom" id="details-menu">
        <h1>Details - Single Click</h1>
        <div class="stats wood-menu-group" v-if="singleClickHistory.length > 0">
          <h2>Game {{showIndex+1}}</h2>
          <div class="animals">
            <animal-square :animal="animal" v-for="animal in getContainedAnimals(showIndex)"
                           :key="animal.name"
                           class="animal-square" :class="singleClickHistory[showIndex].animalToFind.name ===
                           animal.name ? 'animal-highlighted' : ''"/>
          </div>
          <div class="boardNav">
            <div class="btn game-nav-btn" @click="showIndex = loopIndex(showIndex-1)">{{"<"}}</div>
            <div class="tinyBoard border-dark">
              <div class="tile" v-for="coords in getCoords()" :key="coords" :style="getStyle(coords)">
                <img v-if="isClickedTile(coords)" src="/mouse-click.png" :alt="'You Clicked ' + coords"
                     rel="preload"/>
<!--                <span>-->
<!--                  {{(singleClickHistory[showIndex].probabilities[coords.x][coords.y]*100).toFixed(1)}}%-->
<!--                </span>-->
              </div>
              <div class="score">Accuracy: {{(singleClickHistory[showIndex].accuracy*100).toFixed(2)}}%</div>
            </div>
            <div class="btn game-nav-btn" @click="showIndex = loopIndex(showIndex+1)">{{">"}}</div>
          </div>
        </div>

        <div class="nav-btn-container">
          <div class="btn btn-gradient color-action-info" @click="onBack">
            Back
          </div>
          <a class="btn btn-gradient color-action-neutral-2" :href="getReconstructLink()" target="_blank">
            Reconstruct
          </a>
        </div>
      </div>
    </div>
    <menu-bar :on-first-button-click="onHome" first-color-class="color-action-neutral-1" first-button-name="Home"
              :on-second-button-click="onRetry" second-color-class="color-action-good"
              second-button-name="Retry"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-result-details-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.accuracy-single-click-result-details-content {
  flex: 1;
  position: relative;
}
#details-menu {
  max-width: min(90%, 500px);
}
.stats {
  display: flex;
  flex-direction: column;
}

.animals {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-bottom: .5rem;
  gap: .4rem;
}

.animal-square {
  height: 3.5rem;
}

.boardNav {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: .3rem;
  margin-bottom: 1.2rem;
}

.tinyBoard {
  flex-grow: 1;
  aspect-ratio: 1;
  background-color: rgba(0, 0,0 ,0.3);
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  padding: .4rem;
  gap: 3px;
  border-radius: var(--border-radius);
  position: relative;
  max-width: 20rem;
}

.game-nav-btn {
  padding: .7rem .4rem .7rem .4rem;
  background-color: rgba(0, 0, 0, 0.4);
}

.tile {
  aspect-ratio: 1;
  background-color: gray;
  position: relative;
  font-size: .8rem;
  text-align: center;
  align-content: center;
  isolation: isolate;
  box-sizing: unset;
}

.tile > img {
  position: absolute;
  top: 35%;
  left: 20%;
  right: 0;
  bottom: 0;
  margin: auto;
  width: 75%;
  height: 75%;
  filter: drop-shadow(0 10px 4px rgba(0, 0, 0, 0.5));
  z-index: -10;
}

.tile > span {
  z-index: 2;
}

h2 {
  margin-bottom: .8rem;
}

h2, .score {
  text-align: center;
}

.score {
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1.5rem;
}

.nav-btn-container {
  margin:  1rem auto 0 auto;
  display: flex;
  justify-content: space-around;
}

.nav-btn-container > .btn {
  width: 40%;
  padding: 1rem;
  text-align: center;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";
import router from "@/router";
import {Coords} from "@/types/Coords";
import {getHeatmapColor} from "@/util/heatmap-colors";
import {AccuracyGameType} from "@/types/accuracy/AccuracyGameType";
import {Animal} from "@/types/Animal";

const state = useAccuracyState()

export default defineComponent({
  name: "AccuracySingleClickResultDetailsView",
  components: {AnimalSquare, MenuBar},
  data() {
    return {
      singleClickHistory: state.singleClickHistory,
      showIndex: 0
    }
  },

  methods: {
    getContainedAnimals(index: number) {
      return this.singleClickHistory[index].game.containedAnimals
    },

    loopIndex(index: number) {
      if (index < 0) {
        return this.singleClickHistory.length - 1
      } else if (index >= this.singleClickHistory.length) {
        return 0
      } else {
        return index
      }
    },
    getCoords(): Coords[] {
      const element = this.singleClickHistory[this.showIndex]
      if (!element)
        return []
      return element.game.board.flatMap((row:any, y : number) => row.map((_:any, x: number) => ({x, y} as Coords)))
    },

    getStyle(coords: Coords) {
      const element = this.singleClickHistory[this.showIndex]
      if (!element)
        return {}
      const probabilityOfTile = element.probabilities[coords.x][coords.y]
      const heatMapColor = getHeatmapColor(probabilityOfTile, element.minProb, element.maxProb)
      const coordsAreBestClick = element.bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length > 0
      const borderRadiusTopLeft = coords.x === 0 && coords.y === 0 ? "var(--border-radius)" : "0"
      const borderRadiusTopRight = coords.x === element.probabilities.length-1 && coords.y === 0 ?
          "var(--border-radius)" : "0"
      const borderRadiusBottomLeft = coords.x === 0 && coords.y === element.probabilities[0].length-1 ?
          "var(--border-radius)" : "0"
      const borderRadiusBottomRight = coords.x === element.probabilities.length-1 && coords.y === element.probabilities[0].length-1 ?
          "var(--border-radius)" : "0"

      return {
        backgroundColor: heatMapColor,
        border: coordsAreBestClick ? "var(--best-click-border)" : "",
        borderRadius: `${borderRadiusTopLeft} ${borderRadiusTopRight} ${borderRadiusBottomRight} ${borderRadiusBottomLeft}`
      }
    },

    isClickedTile(coords: Coords) {
      const element = this.singleClickHistory[this.showIndex]
      return element.click.x === coords.x && element.click.y === coords.y
    },

    onHome() {
      router.push({name: 'home'})
    },
    onRetry() {
      router.push({name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK + '-play',
        params: {seed: state.seed, region: state.region, difficulty: state.difficulty},
        query: {timeless: state.withTimeless + ""}})
    },
    onBack() {
      router.push({name: 'accuracy-' + AccuracyGameType.SINGLE_CLICK + '-result'})
    },

    getReconstructLink() {
      const element = this.singleClickHistory[this.showIndex]
      const gameRegion = element.game.region.toLowerCase()
      const animals =
          element.game.containedAnimals.map((animal: Animal) => `${animal.name}`.toLowerCase()).join(",")
      return `/reconstruct/${gameRegion}?animals=${animals}`
    }
  },

  async created() {
    if (this.singleClickHistory.length === 0) {
      await router.push({name: 'accuracy'})
      return
    }
  }
})
</script>