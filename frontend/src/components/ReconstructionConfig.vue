<template>
  <div class="config-board dock-bottom">
    <div id="title">Select Animals</div>
    <div
        id="select-region"><DropdownSelect :items="possibleRegions"
                                           @item-selected="(val : string | null) => selectedRegion = val?val : ''"/></div>
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
  name: 'reconstruction-config',
  components: {DropdownSelect: Dropdown},
  props: {

  },
  data() {
    return {
      selectedAnimals : [] as string[],
      regionAnimals : [] as Animal[],
      possibleRegions: [] as string[],
      selectedRegion: ''
    };
  },
  async created() {
    this.possibleRegions = await regionStore.getAllRegions(false);
    await this.onRegionChange(this.possibleRegions[0]);
  },
  watch: {
    selectedRegion(newVal, oldVal) {
      if (newVal !== oldVal)
        this.onRegionChange(newVal);
    },
  },
  methods: {
    async onRegionChange(newRegion: string) {
      this.regionAnimals = await animalStore.getAnimalsOfRegion(newRegion)
    },
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.config-board > * {
  margin-bottom: 1rem;
}
#title {
  font-size: var(--font-size-larger);
}
</style>
