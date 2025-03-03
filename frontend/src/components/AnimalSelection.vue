<template>
  <div class="config-board dock-bottom">
    Select Animals
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";

const animalStore = useAnimals();

export default defineComponent({
  name: 'AnimalSelection',
  props: {
    region: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      selectedAnimals : [] as string[],
      regionAnimals : [] as Animal[]
    };
  },
  async mounted() {
    await this.onRegionChange(this.region);
  },
  watch: {
    region(newVal, oldVal) {
      if (newVal !== oldVal)
        this.onRegionChange(newVal);
    }
  },
  methods: {
    async onRegionChange(newVal: string) {
      this.regionAnimals = await animalStore.getAnimalsOfRegion(newVal)
      console.log(this.regionAnimals);
    }
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
div {
  padding-top: 0.25rem;
}
</style>
