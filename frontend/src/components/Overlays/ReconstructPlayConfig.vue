<template>
  <div class="reconstruct-play-config wood-menu">
    <h1>Reconstruct - Config</h1>
    <div class="config-options">
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
      <div class="attempt-btns">
        <button id="add-attempts" class="btn btn-gradient"
             :class="canAddAttempts? 'color-action-good' : 'color-action-disabled'"
             @click="canAddAttempts ? $emit('add-attempts') : null">
          +5 Attempts
        </button>
        <button id="remove-attempts" class="btn btn-gradient"
             :class="canRemoveAttempts? 'color-action-bad' : 'color-action-disabled'"
             @click="canRemoveAttempts? $emit('remove-attempts'): null">
          -5 Attempts
        </button>
      </div>
    </div>

  </div>
</template>

<style scoped>
.reconstruct-play-config {
  padding-inline: 1.25rem;
}
.config-options {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  column-gap: 1rem;
  row-gap: 1rem;
  justify-content: space-between;
}
h4 {
  margin-bottom: .5rem;
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

.config-options > *:nth-child(even) .animals {
  justify-content: flex-end;
}


.animal-square {
  max-width: 4rem;
}
.attempt-btns {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  gap: .1rem;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import {Animal} from "@/types/Animal";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";

export default defineComponent ({
  name: 'ReconstructPlayConfig',
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
    },
    canAddAttempts: {
      type: Boolean,
      required: false,
    },
    canRemoveAttempts: {
      type: Boolean,
      required: false,
    },
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