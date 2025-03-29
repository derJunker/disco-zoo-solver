<template>
  <div class="accuracy-streak-view" :style="getBackgroundStyle()">
    {{setRouteValuesToVars($route.params.seed, $route.params.region, $route.params.difficulty,  $route.query.timeless)}}
    <div class="accuracy-streak-content">
      <div class="acc-container" v-if="game && animalToFind">
        <top-info-bar :region="displayRegion">
          <span>
            Game: {{gameRound}}
          </span>
        </top-info-bar>
        <animal-display :tracker="new Map()" :animals="game.containedAnimals" class="animal-display"
                        :animal-to-place="animalToFind" :region="displayRegion"/>
        <div class="disco-board-wrapper">
          <disco-board :game="game" :region="displayRegion"
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
.accuracy-streak-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

* {
  transition: .5s;
}

.accuracy-streak-content {
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
import router from "@/router";
import {defineComponent} from 'vue'
import {useGame} from "@/store/useGame";
import {useAccuracyState} from "@/store/useState";
import {useErrors} from "@/store/useErrors";
import MenuBar from "@/components/MenuBar.vue";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import DiscoBoard from "@/components/Basic/DiscoBoard.vue";
import TopInfoBar from "@/components/TopInfoBar.vue";
import {Game} from "@/types/Game";
import {Animal} from "@/types/Animal";
import {Coords} from "@/types/Coords";
import {AccuracyGameHistoryElement} from "@/types/accuracy/AccuracyGameHistoryElement";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";
import {getRegionColors} from "@/util/region-colors";
import {calculateAccuracy} from "@/util/score-calculator";

const gameApi = useGame()
const accuracyState = useAccuracyState()
const errorState = useErrors()

export default defineComponent({
  name: "accuracy-streak-view",
  components: {TopInfoBar, DiscoBoard, AnimalDisplay, MenuBar},
  data() {
    return {
      gameRound: 0,
      computedRounds: 0,
      seed: null as number | null,
      region: null as string | null,
      displayRegion: null as string | null,
      displayRegionColors: null as {
        primary: string,
        light: string,
        dark: string
      } | null,
      difficulty: null as AccuracyDifficulty | null,
      timeless: false,

      loadingResults: false,

      game: null as Game | null,
      animalToFind: null as Animal | null,
      accuracyHistory: [] as AccuracyGameHistoryElement[],
    }
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
        errorState.addError("Seed or region not set, redirecting to accuracy")
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
      this.displayRegionColors = getRegionColors(this.game.region)
      this.animalToFind = response.animalToFind
    },

    async onCoordsClicked(coords: Coords) {
      const maxGameAmount = 100
      if (this.gameRound >= maxGameAmount) {
        return
      }
      const game = this.game
      const region = this.region
      const animalToFind = this.animalToFind
      const currentGameAmount = this.gameRound
      this.loadingResults = true;
      const resp = await gameApi.accuracyPerformance(this.game!, this.animalToFind!, coords)
      if (!resp) {
        await router.push({name: 'accuracy'})
        return
      }
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
      if (!wasBestClick || this.computedRounds >= maxGameAmount) {
        accuracyState.singleClickHistory = this.accuracyHistory
        accuracyState.withTimeless = this.timeless
        accuracyState.region = this.region
        accuracyState.difficulty = this.difficulty
        await router.push({name: 'accuracy-streak-result'})
        return
      }
      this.gameRound++
      if (this.gameRound < maxGameAmount) {
        await this.nextGame()
        this.loadingResults = false
      } else {
        this.loadingResults = true
      }
    },

  },

  async mounted() {
    await this.nextGame()
  }
})
</script>