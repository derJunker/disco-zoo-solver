<template>
  <div class="config-menu wood-menu">
    <h3>Reconstruct - Config</h3>
    <div class="heatmap-config-container">
      <h4>Heatmap:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="animal" :key="animal" @click="onAnimalHeatmapSelected(animal)"
             :style="(heatMapAnimal?.name === animal.name) ? 'border-color: var(--border-highlight)' : ''">
          <animal-square :animal="animal" class="animal-square"/>
        </div>
      </div>
    </div>
    <div class="place-animal-container">
      <h4>Animal to Place:</h4>
      <div class="animals">
        <div v-for="animal in animals" class="animal" :key="animal" @click="onAnimalPlaceSelected(animal)"
             :style="(placeAnimal?.name === animal.name) ? 'border-color: var(--border-highlight)' : ''">
          <animal-square :animal="animal" class="animal-square"/>
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
  border: var(--border-medium) solid rgba(0, 0, 0, var(--border-dark-opacity));
  border-radius: var(--border-radius);
}

.animal-square {
  max-width: 3rem;
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