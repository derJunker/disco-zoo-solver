<template>
  <div class="play-overlay wood-menu">
    <h1>Play</h1>
    <div class="play-buttons">
      <button class="btn btn-gradient color-action-neutral-2" @click="clickedReconstruct()">Reconstruct</button>
      <button class="btn btn-gradient color-action-neutral-3" @click="clickedAccuracy()">Accuracy</button>
    </div>
  </div>
</template>

<style scoped>
.play-buttons {
  display: flex;
  flex-wrap: wrap;
  column-gap: .5rem;
  row-gap: .3rem;
  justify-content: space-between;
}
.btn {
  padding: .7rem 1rem;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import router from "@/router";

export default defineComponent({
  name: 'play-overlay',
  methods: {
    clickedReconstruct() {
      router.push({name: 'reconstruct'});
    },
    clickedAccuracy() {
      router.push({name: 'accuracy'})
    },
  }
})
</script>