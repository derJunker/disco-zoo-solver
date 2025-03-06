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
import {nothingAnimal} from "@/util/nothing-animal";

const gameStore = useGame();

export default defineComponent({
  name: "ReconstructionBoard",
  components: {BoardInfoDisplay, DiscoBoard, BoardAnimalDisplay},
  methods: {
    async tileClick({x, y}: Coords) {
      let animal: Animal | null | undefined = this.animalToPlace;
      if (animal === nothingAnimal || !animal)
        animal = null;
      this.game = await gameStore.clickReconstruct(this.game!, animal, {x, y} as Coords);
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
    },

    animalForHeatmap: {
      type: Object as () => Animal,
      required: false
    },

    animalToPlace: {
      type: Object as () => Animal,
      required: false
    },
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
    },
  },
})
</script>

<style scoped>
#reconstruction-board {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1rem;
}

#board-animal-display {
  max-height: 20%;
}

#disco-board {
  flex: .7;
}

#board-info-display {
  flex: .05;
}
</style>