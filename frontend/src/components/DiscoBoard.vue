<template>
  <div id="disco-board" class="rounded" :style="'background-color: ' + regionColors.dark" v-if="game">
    <div v-for="(coords) in getCoords()" :key="coords" class="disco-cell" :style="getBackgroundStyling(coords)"
         @click="$emit('tile-click', coords)">
      <img v-if="hasRevealedAnimal(coords)" id="animal-icon" :src="getAnimalImagePath(coords)"
           :alt="game?.board[coords.x][coords.y].animalBoardInstance.animal.name"/>
      <span v-if="probabilities && probabilities[coords.x] && !hasRevealedAnimal(coords)" style="text-wrap: nowrap;
      max-width: 100%;
      overflow: hidden;">
        {{probabilities[coords.x][coords.y].toFixed(3)}}
      </span>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue'
import {Game, Tile} from "@/types/Game";
import {Coords} from "@/types/Coords";
import {useAnimals} from "@/store/useAnimals";

const animalStore = useAnimals();

export default defineComponent ({
  name: 'DiscoBoard',
  props: {
    game: {
      type: Object as () => Game | null,
      required: false
    },
    regionColors: {
      type: Object,
      required: true
    },
    bestClicks: {
      type: Array as () => Coords[],
      required: false
    },
    probabilities: {
      type: Array as () => number[][],
      required: false
    }
  },
  methods: {
    hasRevealedAnimal(coords: Coords): boolean {
      const tile = this.game?.board[coords.x][coords.y]
      return tile !== undefined && tile.animalBoardInstance != null
    },

    getAnimalImagePath(coords: Coords): string {
      const tile = this.game?.board[coords.x][coords.y]
      const boardInstance = tile?.animalBoardInstance
      if (boardInstance == null) {
        return '/animals/placeholder.png'
      }
      return animalStore.getAnimalPictureUrl(tile!.animalBoardInstance.animal!)
    },

    isRevealedWithNoAnimal(coords: Coords): boolean {
      const tile = this.game?.board[coords.x][coords.y]
      return tile != undefined && tile.revealed && tile.animalBoardInstance == null
    },

    getCoords(): Coords[] {
      let coords: Coords[] = []
      for (let y = 0; y < this.game!.board.length; y++) {
        for (let x = 0; x < this.game!.board[y].length; x++) {
          coords.push({x, y})
        }
      }
      return coords
    },

    getBackgroundStyling(coords: Coords): string {
      let backgroundStyling = "background-color: "
      if (this.game?.board[coords.x][coords.y].revealed) {
        backgroundStyling += "rgba(0, 0, 0, 0.2)"
      }
      else if(!this.probabilities || this.probabilities.length == 0) {
        backgroundStyling += this.regionColors.primary
      } else {
        let probability = this.probabilities[coords.x][coords.y]
        let min = Math.min(...this.probabilities.flat())
        let max = Math.max(...this.probabilities.flat())
        backgroundStyling += this.valueToHeatmapColor(probability, min, max)
      }

      if (this.bestClicks?.some(click => click.x === coords.x && click.y === coords.y)) {
        backgroundStyling += "; border: 2px solid white"
      }
      return backgroundStyling + ";"
    },

    valueToHeatmapColor(value: number, min: number, max: number) {
      if (value < min || value > max) {
        throw new Error("Value out of range");
      }

      // Normalize value between 0 and 1
      let normalized = (value - min) / (max - min);

      // Adjust green intensity to make mid-tones more orange
      let greenIntensity = value === min ? 255 : Math.round(255 * (1 - normalized) * 0.6 + 80);
      let redIntensity = Math.round(255 * normalized);

      return `rgb(${redIntensity}, ${greenIntensity}, 0)`;
    }
  },
})
</script>
<style scoped>
#disco-board {
  border: var(--border-small) solid rgba(0, 0, 0, var(--border-dark-opacity));
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  align-content: center;
  gap: .2rem;
  padding: .25rem;
  margin-inline: 3rem;
  aspect-ratio: 1;
  flex-grow: 0;
  align-self: center;
  width: 100%;
}
.disco-cell {
  display: grid;
  place-items: center;
  aspect-ratio: 1 / 1;
  font-size: 2rem;
  text-align: center;
  cursor: pointer;
}
#animal-icon {
  max-width: 90%;
}
</style>