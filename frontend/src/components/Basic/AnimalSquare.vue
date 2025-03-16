<template>
 <div class="animal-square" :style="getStyle()">
    <img :src="getAnimalPictureUrl(animal)" :alt="animal.name"/>
 </div>
</template>

<style scoped>
.animal-square {
  aspect-ratio: 1;
  display: grid;
  place-items: center;
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
        backgroundColor: getRarityColor(this.animal.rarity)
      }
    },
    getAnimalPictureUrl(animal: Animal) {
      return animalStore.getAnimalPictureUrl(animal)
    }
  }
})
</script>