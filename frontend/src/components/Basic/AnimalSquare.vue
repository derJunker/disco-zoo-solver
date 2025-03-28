<template>
 <button class="animal-square" :style="getStyle()">
    <img :src="getAnimalPictureUrl(animal)" :alt="animal.name" rel="preload"/>
 </button>
</template>

<style scoped>
.animal-square {
  aspect-ratio: 1;
  display: grid;
  place-items: center;
  padding: 6px;
}
img {
  max-width: 100%;
  max-height: 100%;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import {getRarityColor} from "@/util/rarity-colors";

const animalStore = useAnimals()

export default defineComponent({
  name: "AnimalSquare",

  props: {
    animal: {
      type: Object as () => Animal,
      required: true
    }
  },

  methods: {
    getStyle() {
      return {
        background:
            `linear-gradient(to bottom, rgba(255, 255, 255, 0.15) 50%, rgba(255, 255, 255, 0) 100%), ${getRarityColor(this.animal.rarity)}`
      }
    },
    getAnimalPictureUrl(animal: Animal) {
      if (animal.name.length == 0)
        return ""
      if (animal.rarity === "PET")
        return animalStore.getPetPictureUrl(animal)
      return animalStore.getAnimalPictureUrl(animal)
    }
  }
})
</script>