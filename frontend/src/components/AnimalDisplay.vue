<template>
  <div class="animal-display">
    <div v-for="animal in animals" :key="animal.name" class="animal-container rounded dock-top border-dark"
         :style="'background-color: ' + calcRegionColors($route.params.region).dark + ';'">
      <img :src="getAnimalPicture(animal)" :alt="animal.name" class="animal-picture"/>
    </div>
  </div>
</template>

<style scoped>
.animal-display {
  display: flex;
  gap: .3rem;
  justify-content: center;
}

.animal-container {
  padding: 2rem;
}

.animal-picture {
  width: 4rem;
  max-width: 60px;
  max-height: 60px;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import {getRegionColors} from "@/util/region-colors";

const animalStore = useAnimals()

export default defineComponent({
  name: "AnimalDisplay",
  props: {
    animals: {
      type: Array as () => Animal[],
      required: true
    }
  },

  methods: {
    getAnimalPicture(animal: Animal) {
      return animalStore.getAnimalPictureUrl(animal)
    },
    calcRegionColors(region: string) {
      return getRegionColors(region)
    }
  }
})
</script>