<template>
  <div class="disco-board" :style="getBoardStyle()" v-if="game">
    <button
        v-for="coords in getCoords()" :key="coords" class="tile" :style="getTileStyle(coords)"
        :class="bestClicks && bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length
        > 0 ?
             'best-click' : ''" @click="onCoordsClicked(coords)" @contextmenu="onCoordsRightClick($event, coords)">
      <AnimalSquare
          v-if="game.board[coords.x][coords.y].occupied && game.board[coords.x][coords.y].revealed"
          :animal="game.board[coords.x][coords.y].animalBoardInstance.animal" class="animal-square" />
<!--                <span style="user-select: none;" v-else-if="probabilities">{{probabilities[coords.x][coords.y].toFixed-->
<!--                (3)}}</span>-->
    </button>
    <transition name="fade">
      <div class="loading" v-if="showLoading">
        <loading-circle/>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import AnimalSquare from "@/components/Basic/AnimalSquare.vue"
import {Coords} from "@/types/Coords";
import {Game} from "@/types/Game";
import {getRegionColors} from "@/util/region-colors";
import {getHeatmapColor} from "@/util/heatmap-colors";
import {defineComponent} from "vue";
import LoadingCircle from "@/components/Basic/LoadingCircle.vue";
import {RegionColors} from "@/types/RegionColors";

export default defineComponent({
  name: 'disco-board',
  components: {LoadingCircle, AnimalSquare},

  props: {
    game: {
      type: Object as () => Game | null,
      required: false
    },
    bestClicks: {
      type: Array as () => Coords[],
      required: false
    },
    regionColors: {
      type: Object as () => RegionColors,
      required: false
    },
    probabilities: {
      type: Array as () => number[][],
      required: false
    },
    minProb: {
      type: Number,
      required: false
    },
    maxProb: {
      type: Number,
      required: false
    },
    loading: {
      type: Boolean,
      required: false,
      default: false
    },
  },

  data() {
    return {
      showLoading: this.loading
    }
  },

  watch: {
    loading(newVal: boolean) {
      if (newVal) {
        setTimeout(() => {
          if (this.loading) {
            this.showLoading = true;
          }
        }, 100);
      } else {
        this.showLoading = false;
      }
    }
  },

  methods: {
    getCoords(): Coords[] {
      if (!this.game) {
        return []
      }
      return this.game.board.flatMap((row:any, y : number) => row.map((_:any, x: number) => ({x, y} as Coords)))
    },

    onCoordsClicked(coords: Coords) {
      if(this.loading)
        return
      this.$emit('clicked-coords', coords)
    },

    onCoordsRightClick(event: MouseEvent, coords: Coords) {
      event.preventDefault()
      if (this.loading)
        return
      this.$emit('right-clicked-coords', coords)
    },

    getBoardStyle() {
      if (!this.game) {
        return {}
      }
      return {backgroundColor: this.regionColors?.dark || "black"}
    },

    getTileStyle(coords: Coords) {
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
          return {backgroundColor: this.regionColors?.primary || "gray"}
        } else {
          const probability = this.probabilities[coords.x][coords.y]
          return {backgroundColor: getHeatmapColor(probability, this.minProb!, this.maxProb!)}
        }
      }
    },
  }
})
</script>
<style scoped>

button:focus {
  transform: scale(99.50%);
  transition-duration: .1s;
}

.disco-board {
  position: relative;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 3px;
  padding: 3px;
  isolation: isolate;
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
  border: var(--best-click-border);
}


.animal-square {
  max-width: 100%;
  max-height: 100%;
  position: absolute;
}

.loading {
  position: absolute;
  z-index: 10;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0,0 ,0.6);
  display: grid;
  place-items: center;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.8s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

</style>