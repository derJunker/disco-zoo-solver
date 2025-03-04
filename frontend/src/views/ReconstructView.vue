<template>
  <div>
    <div id="reconstruct-view-content">
      <reconstruction-board id="reconstruction-board" :selected-animals="selectedAnimals"
                            :selected-region="selectedRegion" :game="game"/>
      <reconstruction-config v-if="!game" id="animal-selection" @selected-animals-changed="(val : Animal[]) =>
      selectedAnimals =
      val"
                             @region-changed="(val : string) =>
      selectedRegion = val"
                             @start="start"/>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import ReconstructionBoard from "@/components/ReconstructionBoard.vue";
import ReconstructionConfig from "@/components/ReconstructionConfig.vue";
import {Animal} from "@/types/Animal";
import {useGame} from "@/store/useGame";
import {Game} from "@/types/Game";

let gameStore = useGame();

export default defineComponent ({
  components: {ReconstructionConfig, ReconstructionBoard},
  methods: {
    async start() {
      this.game = await gameStore.startReconstruct(this.selectedAnimals)
    }
  },

  data() {
    return {
      selectedAnimals: [] as Animal[],
      selectedRegion: '' as string,
      game: null as Game | null,
    }
  },
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