<template>
  <div class="animal-select">
    <div v-for="animal in animals" class="rounded" :key="animal" @click="$emit('animal-select', [animal])">
      {{ animal.name }}
    </div>
  </div>
</template>

<style scoped>

.animal-select {
  background-color: gray;
  padding: 1rem 0;
  margin-inline: 1.5rem;
  user-select: none;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAnimals} from "@/store/useAnimals";
import {Animal} from "@/types/Animal";

const animalStore = useAnimals()

export default defineComponent({
  name: "AnimalSelect",
  data() {
    return {
      animals: [] as Animal[]
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
      this.animals = await animalStore.getAnimalsOfRegion(region)
    }
  }
})
</script>