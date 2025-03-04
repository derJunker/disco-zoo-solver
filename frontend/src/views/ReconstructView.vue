<template>
  <div>
    <div id="reconstruct-view-content">
      <reconstruction-board id="reconstruction-board" :selected-animals="selectedAnimals" :selected-region="selectedRegion"/>
      <reconstruction-config id="animal-selection" @selected-animals-changed="(val : Animal[]) => selectedAnimals = val"
                             @region-changed="(val : string) =>
      selectedRegion = val"
                             @start="stop"/>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import ReconstructionBoard from "@/components/ReconstructionBoard.vue";
import ReconstructionConfig from "@/components/ReconstructionConfig.vue";
import {Animal} from "@/types/Animal";

export default defineComponent ({
  components: {ReconstructionConfig, ReconstructionBoard},
  methods: {

  },

  data() {
    return {
      selectedAnimals: [] as Animal[],
      selectedRegion: '' as string
    }
  }
})
</script>

<style scoped>
#reconstruct-view-content {
  display: grid;
  grid-template-columns: 5fr 7fr 5fr;
  height: 100%;
}

#reconstruction-board {
  grid-column: 2;
  height: 100%;
}

#animal-selection {
  margin-right: 2rem;
  margin-left: 2rem;
  height: 55%;
  align-self: end;
}
</style>