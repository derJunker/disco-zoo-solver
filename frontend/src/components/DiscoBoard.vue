<template>
  <div id="disco-board" class="rounded" :style="'background-color: ' + regionColors.dark" v-if="game">
    <div v-for="(coords) in getCoords()" :key="coords" class="disco-cell" :style="'background-color: ' +
    regionColors.primary" @click="$emit('tile-click', coords)">
      <img v-if="hasRevealedAnimal(coords)" id="animal-icon" src="https://placehold.co/400/png" alt="animal"
           style="max-width: 70%;"/>
      <span v-else-if="isRevealedWithNoAnimal(coords)">:(</span>
      <span v-else>{{coords.x}} {{coords.y}}</span>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue'
import {Game, Tile} from "@/types/Game";
import {Coords} from "@/types/Coords";


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
    }
  },
  methods: {
    hasRevealedAnimal(coords: Coords): boolean {
      const tile = this.game?.board[coords.x][coords.y]
      return tile !== undefined && tile.animalBoardInstance != null
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
    }
  }
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
  display: flex;
  justify-content: center;
  align-items: center;
  aspect-ratio: 1 / 1;
  font-size: 2rem;
  text-align: center;
  cursor: pointer;
}
.picture {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style>