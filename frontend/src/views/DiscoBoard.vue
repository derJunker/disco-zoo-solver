<template>
  <div class="disco-board" :style="getBoardStyle()" v-if="game">
    <div
        v-for="coords in getCoords()" :key="coords" class="tile" :style="getTileStyle(coords)"
        :class="bestClicks && bestClicks.filter((click: Coords) => click.x === coords.x && click.y === coords.y).length
        > 0 ?
             'best-click' : ''" @click="onCoordsClicked(coords)" @contextmenu="$emit('right-clicked-coords', $event,
             coords)">
      <AnimalSquare
          v-if="game.board[coords.x][coords.y].occupied && game.board[coords.x][coords.y].revealed"
          :animal="game.board[coords.x][coords.y].animalBoardInstance.animal" class="animal-square" />
      <!--          <div style="user-select: none;" v-else-if="probabilities">{{probabilities[coords.x][coords.y].toFixed(3)}}</div>-->
    </div>
  </div>
</template>

<script lang="ts">
import AnimalSquare from "@/components/Basic/AnimalSquare.vue"
import {Coords} from "@/types/Coords";
import {Game} from "@/types/Game";
import {getRegionColors} from "@/util/region-colors";
import {getHeatmapColor} from "@/util/heatmap-colors";
import {defineComponent} from "vue";

export default defineComponent({
  name: 'disco-board',
  components: {AnimalSquare},

  props: {
    game: {
      type: Object as () => Game | null,
      required: true
    },
    bestClicks: {
      type: Array as () => Coords[],
      required: false
    },
    region: {
      type: String,
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
      this.$emit('clicked-coords', coords)
    },

    getBoardStyle() {
      if (!this.game) {
        return {}
      }
      const regionColors = this.region ? getRegionColors(this.region) : {
        dark: "black", light: "white", primary: "gray"
      }
      return {backgroundColor: regionColors.dark}
    },

    getTileStyle(coords: Coords) {
      const regionColors = this.region ? getRegionColors(this.region) : {
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
  }
})
</script>
<style scoped>

.disco-board {
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


.animal-square {
  max-width: 100%;
  max-height: 100%;
  position: absolute;
}

</style>