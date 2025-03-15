<template>
  <div class="reconstruct-play-view">
    {{ animals }}
  </div>
</template>

<style scoped>

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";

const animalStore = useAnimals()

const url = new URL(window.location.href);
const queryParams = new URLSearchParams(url.search);

export default defineComponent({
  name: "ReconstructPlayView",
  data() {
    return {
      animals: [] as Animal[]
    }
  },

  created() {
    const animalNames = queryParams.get("animals")?.split(",") || []
    animalStore.getAnimalsByNames(animalNames).then(animals => {
      this.animals = animals
    })
  }
})
</script>