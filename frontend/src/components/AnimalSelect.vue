<template>
  <div class="animal-select wood-menu">
    <h3> Select Animals -  {{region}}</h3>
    <div class="animal-select-container">
      <div class="common-animals-container">
        <h4>Common:</h4>
        <div class="common-animals animals">
          <div v-for="animal in commonAnimals" class="rounded animal" :key="animal" @click="onAnimalSelected(animal)"
               :style="isHighlighted(animal) ? 'border-color: var(--border-highlight)' : ''">
            <animal-square :animal="animal" class="animal-picture"/>
          </div>
        </div>
      </div>
      <div class="rare-animals-container">
        <h4>Rare:</h4>
        <div class="rare-animals animals">
          <div v-for="animal in rareAnimals" class="rounded animal" :key="animal" @click="onAnimalSelected(animal)"
               :style="isHighlighted(animal) ? 'border-color: var(--border-highlight)' : ''">
            <animal-square :animal="animal" class="animal-picture"/>
          </div>
        </div>
      </div>
      <div v-if="epicAnimal" class="epic-animal-container">
        <h4>Epic:</h4>
        <div class="epic-animals animals">
          <div class="rounded animal" @click="onAnimalSelected(epicAnimal)"
               :style="isHighlighted(epicAnimal) ? 'border-color: var(--border-highlight)' : ''">
            <animal-square :animal="epicAnimal" class="animal-picture"/>
          </div>
        </div>
      </div>
      <div v-if="timelessAnimal" class="timeless-animal-container">
        <h4>Timeless:</h4>
        <div class="timeless-animals animals">
          <div class="rounded animal" @click="onAnimalSelected(timelessAnimal)"
               :style="isHighlighted(timelessAnimal) ? 'border-color: var(--border-highlight)' : ''">
            <animal-square :animal="timelessAnimal" class="animal-picture"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.animal-select {
  display: grid;
  align-self: stretch;
}

.animal-select-container {
  padding: 1rem 0;
  user-select: none;
  display: grid;
  grid-template-columns: auto auto;
  padding-inline: 1rem;
  gap: .3rem;
}

h4 {
  margin-bottom: .5rem;
}

.animals {
  display: flex;
  flex-wrap: wrap;
  text-align: start;
  gap: 3px;
}

.rare-animals, .timeless-animals {
  justify-self: end;
}

.rare-animals-container, .timeless-animal-container {
  justify-self: end;
  text-align: end;
}

.animal {
  border: var(--border-medium) solid rgba(0, 0, 0, var(--border-dark-opacity));
}

.animal-picture {
  width: 3rem;
  max-width: 100%;
  max-height: 100%;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAnimals} from "@/store/useAnimals";
import {Animal} from "@/types/Animal";
import {useState} from "@/store/useState";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";

const animalStore = useAnimals()
const state = useState()

export default defineComponent({
  name: "AnimalSelect",
  components: {AnimalSquare},
  data() {
    return {
      commonAnimals: [] as Animal[],
      rareAnimals: [] as Animal[],
      epicAnimal: null as Animal | null,
      timelessAnimal: null as Animal | null,
      selectedAnimals: [] as Animal[]
    }
  },
  props: {
    region: {
      type: String,
      required: true
    }
  },

  async created() {
    await this.onRegionChange(this.region)
  },

  watch: {
    async region(newVal, oldVal) {
      await this.onRegionChange(newVal)
    }
  },

  methods: {
    async onRegionChange(region: string) {
      const animals = await animalStore.getAnimalsOfRegion(region)
      this.commonAnimals = animals.filter(a => a.rarity === "COMMON")
      this.rareAnimals = animals.filter(a => a.rarity === "RARE")
      this.epicAnimal = animals.find(a => a.rarity === "EPIC")!
      this.timelessAnimal = animals.find(a => a.rarity === "TIMELESS")!
      state.selectedAnimals = []
      this.syncLocalSelectedAnimals()
    },

    onAnimalSelected(animal: Animal) {
      if (state.selectedAnimals.filter(a => a.name === animal.name).length > 0) {
        state.selectedAnimals = state.selectedAnimals.filter(a => a.name !== animal.name)
      } else {
        if (state.selectedAnimals.length == 3) {
          state.selectedAnimals.shift()
        }

        state.selectedAnimals.push(animal)
      }
      this.syncLocalSelectedAnimals()
    },

    syncLocalSelectedAnimals() {
      this.selectedAnimals = state.selectedAnimals
    },

    getAnimalPicture(animal: Animal) {
      return animalStore.getAnimalPictureUrl(animal)
    },

    isHighlighted(animal: Animal) {
      return state.selectedAnimals.filter(a => a.name === animal.name).length > 0
    }
  }
})
</script>