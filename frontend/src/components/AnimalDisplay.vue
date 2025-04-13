<template>
  <div class="animal-display">
    <div v-for="animal in animals" :key="animal.name" class="animal-container dock-top border-dark"
         :class="animalToPlace?.name === animal.name ? 'highlighted' : ''"
         :style="'background-color: ' + regionColors?.dark + ';'"
         @click="$emit('animal-click', animal)">
      <img :src="getAnimalPicture(animal)" :alt="animal.name" class="animal-picture" rel="preload"/>
      <div class="animal-name">
        {{animal.name}}
      </div>
      <div class="cover-tracker">
        <div v-for="(_, index) in animal.pattern" :key="index" class="square" :class="index < tracker.get(animal) ?
         'covered' : ''">

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.animal-display {
  display: flex;
  gap: .3rem;
  justify-content: center;
  position: relative;
}

.animal-container {
  display: grid;
  place-items: center;
  width: 8rem;
  min-height: 7rem;
  border-radius: var(--border-radius);
  border-color: rgba(0,0,0, 0.2);
}

.highlighted {
  border-color: white;
}

.animal-picture {
  width: 4rem;
  max-width: 60px;
  max-height: 60px;
  user-select: none;
}

.animal-name {
  margin-bottom: .4rem;
  text-align: center;
}

.cover-tracker {
  position: absolute;
  bottom: -.5rem;
  display: flex;
  justify-content: center;
  gap: 3px;
}

.square {
  width: 1rem;
  height: 1rem;
  background-color: black;
  border: white solid 2px;
  border-radius: 3px;
}

.covered {
  background-color: white;
  border-color: black;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import {getRegionColors} from "@/util/region-colors";
import {RegionColors} from "@/types/RegionColors";

const animalStore = useAnimals()

export default defineComponent({
  name: "AnimalDisplay",
  props: {
    animals: {
      type: Array as () => Animal[],
      required: true
    },
    tracker: {
      type: Object as () => Map<Animal, number>,
      required: true
    },
    animalToPlace: {
      type: Object as () => Animal | null,
      required: false,
      default: null
    },
    regionColors: {
      type: Object as () => RegionColors,
      required: false
    }
  },

  methods: {
    getAnimalPicture(animal: Animal) {
      if (animal.rarity === "PET")
        return animalStore.getPetPictureUrl(animal)
      return animalStore.getAnimalPictureUrl(animal)
    },
  }
})
</script>