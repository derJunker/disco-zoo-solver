<template>
  <div class="config-board dock-bottom">
    <div id="title">Select Animals</div>
    <div class="first-row">
      <DropdownSelect :items="possibleRegions"
      @item-selected="(val : string | null) => selectedRegion = val?val : ''"/>
      <ToggleSelect title="Timeless" selected-color-class="timeless" @selected="onTimelessChange" :default-value="false"/>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import Dropdown from "@/components/Basic/Dropdown.vue";
import {useRegions} from "@/store/useRegions";
import ToggleSelect from "@/components/Basic/ToggleSelect.vue";

const animalStore = useAnimals();
const regionStore = useRegions();

export default defineComponent({
  name: 'reconstruction-config',
  components: {ToggleSelect, DropdownSelect: Dropdown},
  props: {

  },
  data() {
    return {
      possibleRegions: [] as string[],
      selectedRegion: '',

      selectedAnimals : [] as string[],
      regionAnimals : [] as Animal[],

      selectedTimeless : false,
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

    onTimelessChange(newVal: boolean) {
      this.selectedTimeless = newVal;
    }
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.first-row {
  display: flex;
  justify-content: space-between;
}

.config-board > * {
  margin-bottom: 1rem;
}
#title {
  font-size: var(--font-size-larger);
}
</style>
