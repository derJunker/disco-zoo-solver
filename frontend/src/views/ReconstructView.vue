<template>
  <div>
    <div id="reconstruct-view-content">
      <reconstruction-board id="reconstruction-board" :selected-animals="selectedAnimals"
                            :selected-region="selectedRegion" :initialGame="game"/>
      <reconstruction-config v-if="!game" id="reconstruction-config"
                             @selected-animals-changed="(val : Animal[]) => selectedAnimals = val"
                             @region-changed="(val : string) => selectedRegion = val"
                             @start="start"/>
      <reconstruction-play-config v-if="game" id="reconstruction-play-config" :animals="selectedAnimals"
                                  :animal-to-place-changed="(val: Animal) => console.log(val)"
                                  :animal-for-heatmap-changed="(val: Animal) => console.log(val)"/>
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
import ReconstructionPlayConfig from "@/components/ReconstructionPlayConfig.vue";

let gameStore = useGame();

export default defineComponent ({
  components: {ReconstructionPlayConfig, ReconstructionConfig, ReconstructionBoard},
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

#reconstruction-config, #reconstruction-play-config {
  margin-right: 2rem;
  margin-left: 2rem;
  height: 55%;
  align-self: end;
}
</style>