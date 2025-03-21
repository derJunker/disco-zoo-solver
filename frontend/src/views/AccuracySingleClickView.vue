<template>
  <div class="accuracy-single-click-view" :style="getBackgroundStyle()">
    {{setRouteValuesToVars($route.params.seed, $route.params.region,  $route.query.timeless)}}
    <div class="accuracy-single-click-content" v-if="game && animalToFind">
      <animal-display :tracker="new Map()" :animals="game.containedAnimals" class="animal-display" :animal-to-place="animalToFind"/>
      <disco-board :game="game" :region="region"
                   class="disco-board" @clicked-coords="onCoordsClicked"
                   @right-clicked-coords="onCoordsClicked"/>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="info"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-click-content {
  position: relative;
  flex: 1;
  display: grid;
  place-items: center;
}


.disco-board {
  max-width: min(90%, 400px);
  margin-inline: auto;
  margin-top: 3rem;
}

.animal-display {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import {Game} from "@/types/Game";
import {useGame} from "@/store/useGame";
import {AccuracySingleClickGameResponse} from "@/types/AccuracySingleClickGameResponse";
import {Animal} from "@/types/Animal";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import DiscoBoard from "@/views/DiscoBoard.vue";
import {getRegionColors} from "@/util/region-colors";
import {Coords} from "@/types/Coords";
import {AccuracyGameHistoryElement} from "@/types/AccuracyGameHistoryElement";
import {useAccuracyState} from "@/store/useState";

const gameApi = useGame()
const accuracyState = useAccuracyState()

export default defineComponent({
  name: "AccuracyPlayView",
  components: {DiscoBoard, AnimalDisplay, MenuBar},
  data() {
    return {
      gameRound: 0,
      seed: null as number | null,
      region: null as string | null,
      timeless: false,

      game: null as Game | null,
      animalToFind: null as Animal | null,
      accuracyHistory: [] as AccuracyGameHistoryElement[]
    }
  },
  methods: {
    onBack() {
      router.push({name: 'accuracy'})
    },

    setRouteValuesToVars(seed: number, region: string, timeless: boolean | null) {
      this.seed = seed
      this.region = region
      if (timeless !== null) {
        this.timeless = timeless
      }
      return ""
    },

    getBackgroundStyle() {
      if (!this.region) {
        return {}
      }
      const regionColors = getRegionColors(this.region)
      return {backgroundColor: regionColors.light}
    },

    async nextGame() {
      if (this.seed === null || this.region === null) {
        await router.push({name: 'accuracy'})
        return
      }
      let response: AccuracySingleClickGameResponse = await gameApi.accuracySingleClick(this.seed!, this.region!,
          this.timeless, this.gameRound)
      this.game = response.game
      this.animalToFind = response.animalToFind
    },

    async onCoordsClicked(coords: Coords) {
      const maxRounds = 2
      gameApi.accuracyPerformance(this.game!, this.animalToFind!, coords).then(resp => {
        this.accuracyHistory.push({
          performance: resp,
          game: this.game!,
          region: this.region!,
          animalToFind: this.animalToFind!
        })
        if (this.accuracyHistory.length >= maxRounds) {
          accuracyState.singleClickHistory = this.accuracyHistory
          router.push({name: 'accuracy-single-click-result', params: {seed: this.seed!.toString(), region: this.region!}})
        }
      })
      this.gameRound++
      if (this.gameRound < maxRounds) {
        await this.nextGame()
      }
    }
  },

  async mounted() {
    await this.nextGame()
  }
})
</script>