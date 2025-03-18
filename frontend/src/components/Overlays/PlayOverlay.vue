<template>
  <div class="play-overlay">
    <h3>Play</h3>
    <div class="play-buttons">
      <div class="btn btn-gradient color-action-neutral-2" @click="clickedReconstruct()">Reconstruct</div>
      <div class="btn btn-gradient color-action-neutral-3" @click="clickedAccuracy()">Accuracy</div>
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