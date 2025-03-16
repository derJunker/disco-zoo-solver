<template>
  <div class="animal-select">
    <div class="common-animals-container">
      <h3>Common:</h3>
      <div class="common-animals animals">
        <div v-for="animal in commonAnimals" class="rounded animal" :key="animal" @click="onAnimalSelected(animal)"
        :style="isHighlighted(animal) ? 'border-color: var(--border-highlight)' : ''">
          <img :src="getAnimalPicture(animal)" :alt="animal.name" class="animal-picture"/>
        </div>
      </div>
    </div>
    <div class="rare-animals-container">
      <h3>Rare:</h3>
      <div class="rare-animals animals">
        <div v-for="animal in rareAnimals" class="rounded animal" :key="animal" @click="onAnimalSelected(animal)"
             :style="isHighlighted(animal) ? 'border-color: var(--border-highlight)' : ''">
          <img :src="getAnimalPicture(animal)" :alt="animal.name" class="animal-picture"/>
        </div>
      </div>
    </div>
    <div v-if="epicAnimal" class="epic-animal-container">
      <h3>Epic:</h3>
      <div class="epic-animals animals">
        <div class="rounded animal" @click="onAnimalSelected(epicAnimal)"
             :style="isHighlighted(epicAnimal) ? 'border-color: var(--border-highlight)' : ''">
          <img :src="getAnimalPicture(epicAnimal)" :alt="epicAnimal.name" class="animal-picture"/>
        </div>
      </div>
    </div>
    <div v-if="timelessAnimal" class="timeless-animal-container">
      <h3>Timeless:</h3>
      <div class="timeless-animals animals">
        <div class="rounded animal" @click="onAnimalSelected(timelessAnimal)"
             :style="isHighlighted(timelessAnimal) ? 'border-color: var(--border-highlight)' : ''">
          <img :src="getAnimalPicture(timelessAnimal)" :alt="timelessAnimal.name" class="animal-picture"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.animal-select {
  background-color: gray;
  padding: 1rem 0;
  margin-inline: 1.5rem;
  user-select: none;
  display: grid;
  grid-template-columns: auto auto;
  padding-inline: 1rem;
  gap: .3rem;
}

h3 {
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
  border: var(--border-small) solid black;
}

.animal-picture {
  width: 40px;
  max-width: 100%;
  max-height: 100%;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAnimals} from "@/store/useAnimals";
import {Animal} from "@/types/Animal";
import {useState} from "@/store/useState";

const animalStore = useAnimals()
const state = useState()

export default defineComponent({
  name: "AnimalSelect",
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