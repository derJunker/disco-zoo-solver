<template>
  <div id="reconstruction-board" class="dock-bottom dock-top border-small" :style="'background-color: ' +
  regionColors.light + ';'">
    <BoardAnimalDisplay :region="selectedRegion" :animals="selectedAnimals"/>
    <DiscoBoard />
    <BoardInfoDisplay />
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import BoardAnimalDisplay from "@/components/BoardAnimalDisplay.vue";
import DiscoBoard from "@/components/DiscoBoard.vue";
import BoardInfoDisplay from "@/components/BoardInfoDisplay.vue";
import {Animal} from "@/types/Animal";
import {getRegionColors} from "@/util/region-colors";


export default defineComponent({
  name: "ReconstructionBoard",
  components: {BoardInfoDisplay, DiscoBoard, BoardAnimalDisplay},
  props: {
    selectedRegion: {
      type: String,
      required: true
    },
    selectedAnimals: {
      type: Array as () => Animal[],
      required: true
    }
  },

  data() {
    return {
      regionColors: {primary: '', dark: '', light: ''}
    }
  },

  created() {
    this.regionColors = getRegionColors(this.selectedRegion);
  },

  watch: {
    selectedRegion(newVal, oldVal) {
      if (newVal !== oldVal)
        this.regionColors = getRegionColors(this.selectedRegion);

      console.log(this.regionColors);
    },
  }
})
</script>

<style scoped>
#reconstruction-board {
  display: grid;
  grid-template-rows: 3fr 10fr 1fr;
  gap: .5rem;
}

#reconstruction-board > * {
  border: black 2px solid;
}
</style>