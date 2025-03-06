<template>
  <div id="disco-board" class="rounded" :style="'background-color: ' + regionColors.dark" v-if="game">
    <div v-for="(row, x) in game.board" :key="row" class="disco-row">
      <div v-for="(tile, y) in row" :key="tile" class="disco-cell" :style="'background-color: ' +
      regionColors.primary" @click="$emit('tile-click', {x, y})">
        <img v-if="hasRevealedAnimal(tile)" id="animal-icon" src="https://placehold.co/400/png" alt="animal"
             style="max-width: 70%;"/>
        <span v-else-if="isRevealedWithNoAnimal(tile)">:(</span>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue'
import {Game, Tile} from "@/types/Game";


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
    hasRevealedAnimal(tile: Tile): boolean {
      return tile.animalBoardInstance != null
    },

    isRevealedWithNoAnimal(tile: Tile): boolean {
      return tile.revealed && tile.animalBoardInstance == null
    }
  }
})
</script>
<style scoped>
#disco-board {
  margin: auto;
  border: var(--border-small) solid rgba(0, 0, 0, var(--border-dark-opacity));
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  align-content: center;
  gap: .2rem;
  padding: .25rem;
  margin-inline: 1rem;
  aspect-ratio: 1;
  width: 70vmin;
  height: 70vmin;
  max-width: 30rem;
  max-height: 30rem;
  align-self: center;
}
.disco-row {
  display: contents;
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