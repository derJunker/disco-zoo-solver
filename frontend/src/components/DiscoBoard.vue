<template>
  <div id="disco-board" class="rounded" :style="'background-color: ' + regionColors.dark" v-if="game">
    <div v-for="(row, x) in game.board" :key="row" class="disco-row">
      <div v-for="(tile, y) in row" :key="tile" class="disco-cell" :style="'background-color: ' + regionColors.primary">
        <span class="picture" @click="$emit('tile-click', {x, y})">
          {{ getTileRepr(tile) }}
        </span>
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
    getTileRepr(tile: Tile) {
      if (tile.revealed) {
        if(tile.animalBoardInstance)
          return tile.animalBoardInstance.animal.name.substring(0,2)
        else
          return 'x'
      } else {
        if(tile.animalBoardInstance)
          return '?'
      }
      return ''
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