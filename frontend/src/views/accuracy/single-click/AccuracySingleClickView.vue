<template>
  <div class="accuracy-single-click-view" :style="getBackgroundStyle()">
    {{setRouteValuesToVars($route.params.seed, $route.params.region, $route.params.difficulty,  $route.query.timeless)}}
    <div class="accuracy-single-click-content">
      <div class="acc-container" v-if="game && animalToFind">
        <top-info-bar :region="displayRegionColors">
          <span>
            Game: {{limitedGameRound() + 1}}
          </span>
        </top-info-bar>
        <animal-display :tracker="new Map()" :animals="game.containedAnimals" class="animal-display"
                        :animal-to-place="animalToFind" :region-colors="displayRegionColors"/>
        <div class="disco-board-wrapper">
          <disco-board :game="game" :region-colors="displayRegionColors"
                       class="disco-board" @clicked-coords="onCoordsClicked"
                       @right-clicked-coords="onCoordsClicked" :loading="loadingResults"/>
        </div>
      </div>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="onHome" second-color-class="color-action-info"
              second-button-name="Home"/>
  </div>
</template>

<style scoped>
* {
  transition: background-color .5s;
}


.accuracy-single-click-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-click-content {
  position: relative;
  flex: 1;
}

.acc-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
}


.disco-board-wrapper {
  align-content: center;
  margin-top: auto;
  margin-bottom: auto;
}

.disco-board {
  max-width: min(90%, 400px, 55dvh);
  margin-inline: auto;
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import {Game} from "@/types/Game";
import {useGame} from "@/store/useGame";
import {Animal} from "@/types/Animal";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import DiscoBoard from "@/components/Basic/DiscoBoard.vue";
import {getRegionColors} from "@/util/region-colors";
import {Coords} from "@/types/Coords";
import {AccuracyGameHistoryElement} from "@/types/accuracy/AccuracyGameHistoryElement";
import {useAccuracyState} from "@/store/useState";
import {calculateAccuracy} from "@/util/score-calculator";
import TopInfoBar from "@/components/TopInfoBar.vue";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";
import {useErrors} from "@/store/useErrors";
import {RegionColors} from "@/types/RegionColors";

const gameApi = useGame()
const accuracyState = useAccuracyState()
const errorState = useErrors()

export default defineComponent({
  name: "AccuracyPlayView",
  components: {TopInfoBar, DiscoBoard, AnimalDisplay, MenuBar},
  data() {
    return {
      gameRound: 0,
      computedRounds: 0,
      seed: null as number | null,
      region: null as string | null,
      displayRegion: null as string | null,
      displayRegionColors: null as RegionColors | null,
      difficulty: null as AccuracyDifficulty | null,
      timeless: false,

      loadingResults: false,

      game: null as Game | null,
      animalToFind: null as Animal | null,
      accuracyHistory: [] as AccuracyGameHistoryElement[],
    }
  },

  watch: {
    displayRegion: {
      handler(newVal) {
        this.displayRegionColors = getRegionColors(newVal)
      },
      immediate: true
    },
  },

  methods: {
    onBack() {
      router.push({name: 'accuracy'})
    },

    onHome() {
      router.push({name: 'home'})
    },

    setRouteValuesToVars(seed: number, region: string, difficulty:string,  timeless: boolean | null) {
      this.seed = seed
      this.region = region
      this.difficulty = difficulty as AccuracyDifficulty
      if (timeless !== null) {
        this.timeless = timeless
      }
      return ""
    },

    getBackgroundStyle() {
      if (!this.displayRegionColors) {
        return {}
      }
      return {backgroundColor: this.displayRegionColors.light}
    },

    async nextGame() {
      if (this.seed === null || this.region === null) {
        errorState.addError("Missing seed or region? How did you get here :D")
        await router.push({name: 'accuracy'})
        return
      }
      let response = await gameApi.accuracySingleClick(this.seed!, this.region!,
          this.timeless, this.gameRound, this.difficulty!)
      if (!response) {
        await router.push({name: 'accuracy'})
        return
      }
      this.game = response.game
      this.displayRegion = this.game.region;
      this.animalToFind = response.animalToFind
    },

    async onCoordsClicked(coords: Coords) {
      const maxGameAmount = process.env.VUE_APP_ACCURACY_GAME_AMOUNT
      if (this.gameRound >= maxGameAmount) {
        return
      }
      const game = this.game
      const region = this.region
      const animalToFind = this.animalToFind
      const currentGameAmount = this.gameRound
      gameApi.accuracyPerformance(this.game!, this.animalToFind!, coords).then(resp => {
        if (!resp)
          return
        const wasBestClick = resp.bestClicks.filter(click => click.x == coords.x && click.y == coords.y).length > 0
        const maxProb = Math.max(...resp.probabilities.flat())
        const minProb = Math.min(...resp.probabilities.flat())
        this.accuracyHistory[currentGameAmount] = {
          click: coords,
          accuracy: calculateAccuracy(resp.accuracy, wasBestClick ? 1 : 0),
          animalToFind: animalToFind!,

          game: game!,
          region: region!,
          probabilities: resp.probabilities,
          minProb: minProb,
          maxProb: maxProb,
          bestClicks: resp.bestClicks,
          wasBestClick: wasBestClick,
        }
        this.computedRounds++
        if (this.computedRounds >= maxGameAmount) {
          accuracyState.singleClickHistory = this.accuracyHistory
          accuracyState.withTimeless = this.timeless
          accuracyState.region = this.region
          accuracyState.difficulty = this.difficulty
          router.push({name: 'accuracy-single-click-result'})
        }
      })
      this.gameRound++
      if (this.gameRound < maxGameAmount) {
        await this.nextGame()
      } else {
        this.loadingResults = true
      }
    },

    limitedGameRound() {
      return Math.min(Math.max(0, this.gameRound), process.env.VUE_APP_ACCURACY_GAME_AMOUNT-1)
    }
  },

  async mounted() {
    await this.nextGame()
  }
})
</script>