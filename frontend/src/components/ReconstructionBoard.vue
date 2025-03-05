<template>
  <div id="reconstruction-board" class="dock-bottom dock-top border-small" :style="'background-color: ' +
  regionColors.light + ';'">
    <BoardAnimalDisplay :region="selectedRegion" :animals="selectedAnimals" id="board-animal-display"/>
    <DiscoBoard :game="game" id="disco-board" :region-colors="regionColors" @tile-click="tileClick"/>
    <BoardInfoDisplay id="board-info-display"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import BoardAnimalDisplay from "@/components/BoardAnimalDisplay.vue";
import DiscoBoard from "@/components/DiscoBoard.vue";
import BoardInfoDisplay from "@/components/BoardInfoDisplay.vue";
import {getRegionColors} from "@/util/region-colors";

import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";
import {useGame} from "@/store/useGame";
import {Coords} from "@/types/Coords";

const gameStore = useGame();

export default defineComponent({
  name: "ReconstructionBoard",
  components: {BoardInfoDisplay, DiscoBoard, BoardAnimalDisplay},
  methods: {
    async tileClick({x, y}: Coords) {
      this.game = await gameStore.clickReconstruct(this.game!, null, {x, y} as Coords);
    }
  },
  props: {
    selectedRegion: {
      type: String,
      required: true
    },
    selectedAnimals: {
      type: Array as () => Animal[],
      required: true
    },

    initialGame: {
      type: Object as () => Game,
      required: false
    }
  },

  data() {
    return {
      regionColors: {primary: '', dark: '', light: ''},
      game: null as Game | null
    }
  },

  created() {
    this.regionColors = getRegionColors(this.selectedRegion);
  },

  watch: {
    selectedRegion(newVal, oldVal) {
      if (newVal !== oldVal)
        this.regionColors = getRegionColors(this.selectedRegion);
    },
    initialGame(newVal, oldVal) {
      if (newVal !== oldVal)
        this.game = newVal;
    }
  },
})
</script>

<style scoped>
#reconstruction-board {
  display: grid;
  grid-template-rows: 3fr 10fr 1fr;
  gap: .5rem;
}

#board-animal-display {
  grid-row: 1;
}

#disco-board {
  grid-row: 2;
}

#board-info-display {
  grid-row: 3;
}
</style>