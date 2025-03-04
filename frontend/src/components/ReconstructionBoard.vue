<template>
  <div id="reconstruction-board" class="dock-bottom dock-top border-small">
    <BoardAnimalDisplay />
    <DiscoBoard />
    <BoardInfoDisplay />
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import BoardAnimalDisplay from "@/components/BoardAnimalDisplay.vue";
import DiscoBoard from "@/components/DiscoBoard.vue";
import BoardInfoDisplay from "@/components/BoardInfoDisplay.vue";


export default defineComponent({
  name: "ReconstructionBoard",
  components: {BoardInfoDisplay, DiscoBoard, BoardAnimalDisplay}
})
</script>

<style scoped>
#reconstruction-board {
  display: grid;
  grid-template-rows: 4fr 10fr 1fr;
  gap: .5rem;
  background: var(--farm-light);
}

#reconstruction-board > * {
  border: black 2px solid;
}
</style>