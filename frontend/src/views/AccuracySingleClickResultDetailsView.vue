<template>
  <div class="accuracy-single-click-result-details-view">
    <div class="accuracy-single-click-result-details-content">
      <div class="wood-menu menu-bottom dock-bottom" id="details-menu">
        <h1>Accuracy Single Click Result Details</h1>
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
              <div class="score">Score: {{(singleClickHistory[showIndex].score*100).toFixed(2)}}%</div>
            </div>
            <div class="btn game-nav-btn" @click="showIndex = loopIndex(showIndex+1)">{{">"}}</div>
          </div>
        </div>
      </div>
    </div>
    <menu-bar :on-first-button-click="console.log" first-color-class="color-action-neutral-1" first-button-name="sth"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="sth"/>
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
}

.animal-square {
  max-height: 3.5rem;
}

.boardNav {
  aspect-ratio: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: .3rem;
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

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";
import router from "@/router";
import {Coords} from "@/types/Coords";
import {getHeatmapColor} from "@/util/heatmap-colors";

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
      return {
        backgroundColor: heatMapColor,
        border: coordsAreBestClick ? "var(--best-click-border)" : ""
      }
    },

    isClickedTile(coords: Coords) {
      const element = this.singleClickHistory[this.showIndex]
      return element.click.x === coords.x && element.click.y === coords.y
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