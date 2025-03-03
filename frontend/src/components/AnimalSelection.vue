<template>
  <div class="config-board dock-bottom">
    <div id="title">Select Animals</div>
    <div id="select-region"><DropdownSelect :items="possibleRegions"/></div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import Dropdown from "@/components/Basic/Dropdown.vue";
import {useRegions} from "@/store/useRegions";

const animalStore = useAnimals();
const regionStore = useRegions();

export default defineComponent({
  name: 'AnimalSelection',
  components: {DropdownSelect: Dropdown},
  props: {
    region: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      selectedAnimals : [] as string[],
      regionAnimals : [] as Animal[],
      possibleRegions: [] as string[]
    };
  },
  async created() {
    this.possibleRegions = await regionStore.getAllRegions();
    await this.onRegionChange(this.possibleRegions[0]);
  },
  watch: {
    region(newVal, oldVal) {
      if (newVal !== oldVal)
        this.onRegionChange(newVal);
    },
  },
  methods: {
    async onRegionChange(newVal: string) {
      this.regionAnimals = await animalStore.getAnimalsOfRegion(newVal)
    },

    animalToStringMapper(animal: Animal) {
      if (animal)
        return animal.name;
      return "";
    },
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
