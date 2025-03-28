<template>
  <div class="animal-select">
    <div class="animal-select-container">
      <div class="common-animals-container">
        <h2>Common:</h2>
        <div class="common-animals animals">
          <div v-for="animal in commonAnimals" class="animal" :key="animal" @click="onAnimalSelected(animal)">
            <animal-square :animal="animal" class="animal-picture" :class="isHighlighted(animal) ? 'animal-highlighted' : ''"/>
          </div>
        </div>
      </div>
      <div class="rare-animals-container">
        <h2>Rare:</h2>
        <div class="rare-animals animals">
          <div v-for="animal in rareAnimals" class="animal" :key="animal" @click="onAnimalSelected(animal)">
            <animal-square :animal="animal" class="animal-picture" :class="isHighlighted(animal) ? 'animal-highlighted' : ''"/>
          </div>
        </div>
      </div>
      <div v-if="epicAnimal" class="epic-animal-container">
        <h2>Epic:</h2>
        <div class="epic-animals animals">
          <div class="animal" @click="onAnimalSelected(epicAnimal)">
            <animal-square :animal="epicAnimal" class="animal-picture" :class="isHighlighted(epicAnimal) ? 'animal-highlighted' : ''"/>
          </div>
        </div>
      </div>
      <div v-if="timelessAnimal && timeless" class="timeless-animal-container">
        <h2>Timeless:</h2>
        <div class="timeless-animals animals">
          <div class="animal" @click="onAnimalSelected(timelessAnimal)">
            <animal-square :animal="timelessAnimal" class="animal-picture"
                           :class="isHighlighted(timelessAnimal) ?
            'animal-highlighted' : ''"/>
          </div>
        </div>
      </div>
    </div>
    <div id="pet-select">
      <h2 style="display: inline">Pet:</h2>
      <transition name="fade">
        <span v-if="showPets"
              class="pet-select-options pet border-light animal-square">
          <span v-for="pet in pets" :key="pet.name" @click="selectPet(pet)">
            <img :alt="pet.name" :src="getPetUrl(pet)"/>
          </span>
        </span>
      </transition>
      <button v-if="!showPetSelect" class="btn btn-gradient color-action-good" style="display: inline; padding: .2rem
       .4rem;
      margin-left: .2rem"
              @click="onAddPet">+
      </button>
      <button v-if="showPetSelect" class="btn btn-gradient color-action-bad" style="display: inline; padding: .2rem
      .5rem;
      margin-left: .2rem"
              @click="onRemovePet">-
      </button>
      <button v-if="showPetSelect && selectedPet" id="pet-select-btn" @click="showPets = !showPets"
              class="animal-square pet"
               ref="elementToDetectOutsideClick">
        <span class="pet-img-wrapper">
          <img :alt="selectedPet.name" :src="getPetUrl(selectedPet)"/>
        </span>
        <span class="pet-dropdown-btn">V</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.animal-select-container {
  padding: 1rem 0;
  user-select: none;
  display: grid;
  grid-template-columns: auto auto;
  gap: .3rem;
}

h2 {
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
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.5));
}

.animal:has(.animal-highlighted)  {
  filter: drop-shadow(0 0 4px var(--animal-highlight-color));
}

.animal-picture {
  width: 4rem;
  max-width: 100%;
  max-height: 100%;
}

#pet-select {
  position: relative;
}

#pet-select-btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 4rem;
  width: 5.5rem;
  padding-right: .5rem;
  position: relative;
  margin-top: .4rem;
  z-index: 3;
}

#pet-select-btn img, .pet-select-options img {
  max-width: 100%;
  max-height: 100%;
  padding: 8px 6px 6px 6px;
}

.pet-dropdown-btn {
  transform: rotateX(180deg);
  text-shadow: 1px -2px 3px rgba(0, 0, 0, 0.7);
}

.pet-select-options {
  z-index: 2;
  position: absolute;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  column-gap: 1.5rem;
  width: 10rem;
  bottom: 0;
  overflow: hidden;
}

.pet-select-options > span {
  height: 4rem;
  width: 4rem;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAnimals} from "@/store/useAnimals";
import {Animal} from "@/types/Animal";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";

const animalStore = useAnimals()

const loadingCommonAnimal: Animal = {
  name: "",
  rarity: "COMMON",
  region: "Loading...",
  pattern: []
}

const loadingRareAnimal: Animal = {
  name: "",
  rarity: "RARE",
  region: "Loading...",
  pattern: []
}

const loadingEpicAnimal: Animal = {
  name: "",
  rarity: "EPIC",
  region: "Loading...",
  pattern: []
}

const loadingTimelessAnimal: Animal = {
  name: "",
  rarity: "TIMELESS",
  region: "Loading...",
  pattern: []
}

export default defineComponent({
  name: "AnimalSelect",
  components: {AnimalSquare},
  data() {
    return {
      commonAnimals: [loadingCommonAnimal, loadingCommonAnimal, loadingCommonAnimal] as Animal[],
      rareAnimals: [loadingRareAnimal, loadingRareAnimal] as Animal[],
      epicAnimal: loadingEpicAnimal as Animal | null,
      timelessAnimal: loadingTimelessAnimal as Animal | null,
      pets: [] as Animal[],
      showPets: false,
      showPetSelect: false
    }
  },
  props: {
    region: {
      type: String,
      required: true
    },
    selectedAnimals: {
      type: Array as () => Animal[],
      required: true
    },
    timeless: {
      type: Boolean,
      required: false,
      default: false
    },
    selectedPet: {
      type: Object as () => Animal | null,
      required: false,
      default: null
    }
  },

  async created() {
    await this.onRegionChange(this.region)
    this.pets = await animalStore.getAllPets()
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  watch: {
    async region(newVal, oldVal) {
      await this.onRegionChange(newVal)
    }
  },

  methods: {
    handleClickOutside(event:any) {
      if
      (this.$refs.elementToDetectOutsideClick
          && !(this.$refs.elementToDetectOutsideClick as any).contains(event.target)) {
        this.showPets = false
      }
    },

    getPetUrl(pet: Animal) {
      return animalStore.getPetPictureUrl(pet)
    },

    onAddPet() {
      this.showPetSelect = true
      this.$emit('pet-selected', this.pets[6])
    },

    onRemovePet() {
      this.$emit('pet-selected', null)
      this.showPetSelect = false
    },

    selectPet(pet : Animal) {
      const indexOfCurrent = this.pets.findIndex(p => p.name === this.selectedPet?.name)
      const indexOfNew = this.pets.findIndex(p => p.name === pet.name)
      // Swap indices
      this.pets[indexOfCurrent] = this.pets.splice(indexOfNew, 1, this.pets[indexOfCurrent])[0]
      this.$emit('pet-selected', pet)
    },

    async onRegionChange(region: string) {
      const animals = await animalStore.getAnimalsOfRegion(region)
      if (animals.length == 0)
        return;
      this.commonAnimals = animals.filter(a => a.rarity === "COMMON")
      this.rareAnimals = animals.filter(a => a.rarity === "RARE")
      this.epicAnimal = animals.find(a => a.rarity === "EPIC")!
      this.timelessAnimal = animals.find(a => a.rarity === "TIMELESS")!
      this.$emit('animals-selected', [])
    },

    onAnimalSelected(animal: Animal) {
      if (this.selectedAnimals.filter(a => a.name === animal.name).length > 0) {
        this.$emit('animals-selected', this.selectedAnimals.filter(a => a.name !== animal.name))
      } else {
        let newAnimals = [...this.selectedAnimals]
        if (newAnimals.length == 3 || (newAnimals.length == 1 && this.selectedPet)) {
          newAnimals.shift()
        }

        newAnimals.push(animal)
        this.$emit('animals-selected', newAnimals)
      }
    },


    isHighlighted(animal: Animal) {
      return this.selectedAnimals.filter(a => a.name === animal.name).length > 0
    }
  }
})
</script>