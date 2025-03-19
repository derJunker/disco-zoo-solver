<template>
  <div class="config-menu wood-menu">
    <h1>Reconstruct - Config</h1>
    <div class="heatmap-config-container">
      <h4>Heatmap:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="animal" :key="animal" @click="onAnimalHeatmapSelected(animal)">
          <animal-square :animal="animal" class="animal-square" :class="(heatMapAnimal?.name === animal.name) ?
          'animal-highlighted' : ''"/>
        </div>
      </div>
    </div>
    <div class="place-animal-container">
      <h4>Animal to Place:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="animal" :key="animal" @click="onAnimalPlaceSelected(animal)">
          <animal-square :animal="animal" class="animal-square" :class="(placeAnimal?.name === animal.name) ?
          'animal-highlighted' : ''"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
h4 {
  margin-bottom: .5rem;
}

.place-animal-container {
  margin-top: .5rem;
}

.animals {
  display: flex;
  gap: .2rem;
}

.animal {
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.5));
}

.animal:has(.animal-highlighted)  {
  filter: drop-shadow(0 0 4px var(--animal-highlight-color));
}


.animal-square {
  max-width: 4rem;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import {Animal} from "@/types/Animal";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";

export default defineComponent ({
  name: 'ConfigMenu',
  components: {AnimalSquare},
  props: {
    animals: {
      type: Array as () => Animal[],
      required: true
    },
    heatMapAnimal: {
      type: Object as () => Animal | null,
      required: false,
      default: null
    },
    placeAnimal: {
      type: Object as () => Animal | null,
      required: false,
      default: null
    }
  },

  data() {
    return {
    }
  },


  methods: {
    onAnimalHeatmapSelected(animal: Animal) {
      this.$emit('animal-heatmap-select', animal)
    },

    onAnimalPlaceSelected(animal: Animal | null) {
      this.$emit('animal-place-select', animal)
    },
  }
})
</script>