<template>
  <div class="config-menu rounded border-light">
    <h3>Reconstruct - Config</h3>
    <div class="heatmap-config-container">
      <h4>Heatmap:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="rounded animal" :key="animal" @click="onAnimalHeatmapSelected(animal)"
             :style="(heatMapAnimal === animal) ? 'border-color: var(--border-highlight)' : ''">
          {{ animal.name }}
        </div>
      </div>
    </div>
    <div class="place-animal-container">
      <h4>Animal to Place:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="rounded animal" :key="animal" @click="onAnimalPlaceSelected(animal)"
             :style="(placeAnimal === animal) ? 'border-color: var(--border-highlight)' : ''">
          {{ animal.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
h3 {
  text-align: center;
  margin-bottom: 1rem;
}
.config-menu {
  padding: 1rem;
  background-color: var(--wood-color-dark);
}

.place-animal-container {
  margin-top: .5rem;
}

.animals {
  display: flex;
  gap: .2rem;
}

.animal {
  border: var(--border-small) solid black;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import {Animal} from "@/types/Animal";

export default defineComponent ({
  name: 'ConfigMenu',
  props: {
    animals: {
      type: Array as () => Animal[],
      required: true
    }
  },

  data() {
    return {
      heatMapAnimal: null as Animal | null,
      placeAnimal: null as Animal | null
    }
  },

  beforeMount() {
    this.heatMapAnimal = this.animals[0]
    console.log("created")
    this.$emit('animal-heatmap-select', this.heatMapAnimal)
  },

  methods: {
    onAnimalHeatmapSelected(animal: Animal) {
      this.heatMapAnimal = animal
      this.$emit('animal-heatmap-select', animal)
    },

    onAnimalPlaceSelected(animal: Animal | null) {
      if (this.placeAnimal === animal) {
        animal = null
      }
      this.placeAnimal = animal
      this.$emit('animal-place-select', animal)
    },
  }
})
</script>