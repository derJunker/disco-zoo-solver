<template>
  <div id="disco-board" class="rounded" :style="'background-color: ' + regionColors.dark">
    <div v-for="row in 5" :key="row" class="disco-row">
      <div v-for="col in 5" :key="col" class="disco-cell" :style="'background-color: ' + regionColors.light">
        <span class="picture">
          Ani
        </span>
      </div>
    </div>
    </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue'
import {Game} from "@/types/Game";


export default defineComponent ({
  name: 'DiscoBoard',
  props: {
    game: {
      type: Object as () => Game | null,
      required: true
    },
    regionColors: {
      type: Object,
      required: true
    }
  }
})
</script>
<style scoped>
#disco-board {
  aspect-ratio: 1;
  width: 75%;
  margin: auto;
  border: var(--border-small) solid rgba(0, 0, 0, var(--border-dark-opacity));
  display: grid;
  grid-template-rows: repeat(5, 1fr);
  grid-template-columns: repeat(5, 1fr);
  align-content: center;
  gap: .25rem;
  padding: .25rem;
}
.disco-row {
  display: grid;
  grid-row: span 5;
  grid-template-rows: subgrid;
}
.disco-cell {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
.picture {
  aspect-ratio: 1;
  width: 80%;
}
</style>