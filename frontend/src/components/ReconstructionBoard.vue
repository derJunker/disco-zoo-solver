<template>
  <div id="reconstruction-board" class="dock-bottom dock-top border-small" :style="'background-color: ' +
  regionColors.light + ';'">
    <div id="board-animal-display-wrapper">
      <BoardAnimalDisplay :region="selectedRegion" :animals="selectedAnimals" id="board-animal-display"/>
    </div>
<!--    <new-disco-board id="disco-board"/>-->
    <DiscoBoard :game="game" id="disco-board" :region-colors="regionColors" @tile-click="tileClick" :bestClicks="bestClicks"
        :probabilities="probabilities"/>
    <BoardInfoDisplay id="board-info-display"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import BoardAnimalDisplay from "@/components/BoardAnimalDisplay.vue";
import BoardInfoDisplay from "@/components/BoardInfoDisplay.vue";
import {getRegionColors} from "@/util/region-colors";

import {Animal} from "@/types/Animal";
import {Game} from "@/types/Game";
import {useGame} from "@/store/useGame";
import {Coords} from "@/types/Coords";
import {nothingAnimal} from "@/util/nothing-animal";
import DiscoBoard from "@/components/DiscoBoard.vue";
import {useSolver} from "@/store/useSolver";

const gameStore = useGame();
const solverStore = useSolver();

export default defineComponent({
  name: "ReconstructionBoard",
  components: {DiscoBoard, BoardInfoDisplay, BoardAnimalDisplay},
  methods: {
    async tileClick({x, y}: Coords) {
      let animal: Animal | null | undefined = this.animalToPlace;
      if (!animal || animal.name === nothingAnimal.name) {
        animal = null;
      }
      this.game = await gameStore.clickReconstruct(this.game!, animal, {x, y} as Coords);
      const { bestClicks, probabilities } = await solverStore.solve(this.game!, this.animalForHeatmap!);
      console.log("bestClicks")
      console.log(bestClicks)
      this.bestClicks = bestClicks;
      this.probabilities = probabilities;
    },
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
      game: null as Game | null,
      bestClicks: [] as Coords[],
      probabilities: [] as number[][],
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
      if (newVal !== oldVal) {
        this.game = newVal;
      }
    },
    async animalForHeatmap(newVal, oldVal) {
      if (newVal !== oldVal) {
        const { bestClicks, probabilities } = await solverStore.solve(this.game!, this.animalForHeatmap!);
        console.log("bestClicks")
        console.log(bestClicks)
        this.bestClicks = bestClicks;
        this.probabilities = probabilities;
      }
    },
  },
})
</script>

<style scoped>
#reconstruction-board {
  position: relative;
  display: grid;
  place-items: center;
}

#board-animal-display-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
}

#board-info-display {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
}


#disco-board {
  width: 66%;
  margin-top: 10rem;
}
</style>