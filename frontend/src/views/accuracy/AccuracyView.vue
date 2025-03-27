<template>
  <div class="accuracy-view">
    <div class="accuracy-content">
      <transition name="overlay">
        <accuracy-config v-if="!showRegionSelect" class="accuracy-config dock-bottom menu-bottom"
                         :selected-region="selectedRegion" :selected-game-type="selectedGameType" :timeless="timeless"
                         :selected-difficulty="selectedDifficulty"
                         @timeless-changed="onTimelessChanged"
                         @region-clicked="onRegionClicked" @game-type-selected="onGameTypeSelected"
                         @difficulty-selected="onDifficultySelected"/>
        <region-select v-else-if="showRegionSelect"  @region-select="onRegionSelect"
                        class="region-select dock-bottom dock-bottom-shadow menu-bottom" :any-option-available="true"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onBack" :on-second-button-click="showRegionSelect? onCloseRegionSelect : onPlay" :second-button-name="showRegionSelect? 'close' : 'play'"
              first-button-name="back" first-color-class="color-action-neutral-1"
              :second-color-class="showRegionSelect? 'color-action-bad' : 'color-action-neutral-2'"/>
  </div>
</template>

<style scoped>
.accuracy-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-content {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.accuracy-config {
 max-width: min(90%, 500px);
}
.region-select {
  max-width: min(90%, 750px);
}


</style>

<script lang="ts">
import {defineComponent} from "vue";
import MenuBar from "@/components/MenuBar.vue";
import AccuracyConfig from "@/components/Overlays/AccuracyConfig.vue";
import router from "@/router";
import RegionSelect from "@/components/Overlays/RegionSelect.vue";
import {AccuracyGameType} from "@/types/accuracy/AccuracyGameType";
import {AccuracyDifficulty} from "@/types/accuracy/AccuracyDifficulty";
import {useAccuracyState, userSettingsState} from "@/store/useState";
import {generateSeed} from "@/util/seed-generator";

const userSettings = userSettingsState()
const accuracyState = useAccuracyState()

export default defineComponent({
  name: "AccuracyView",
  components: {RegionSelect, AccuracyConfig, MenuBar},
  data() {
    return {
      selectedRegion: accuracyState.getLastSelectedRegion || "farm" as string | null,
      selectedGameType: accuracyState.getLastSelectedGameType ||AccuracyGameType.SINGLE_CLICK,
      selectedDifficulty: accuracyState.getLastSelectedDifficulty || AccuracyDifficulty.MEDIUM,
      showRegionSelect: false,
      timeless: userSettings.isTimelessRegionAllowed(accuracyState.getLastSelectedRegion || "farm")
    }
  },
  methods: {
    onBack() {
      router.push({name: 'home'})
    },
    onPlay() {
      router.push({name: 'accuracy-' + this.selectedGameType +'-play',
        params: {seed: generateSeed(), region:
          this.selectedRegion, difficulty: this.selectedDifficulty},
        query: {timeless: this.timeless + ""}})
    },

    onCloseRegionSelect() {
      this.showRegionSelect = false
    },

    onRegionClicked() {
      this.showRegionSelect = true
    },

    onGameTypeSelected(gameType: string) {
      this.selectedGameType = gameType
      accuracyState.setLastSelectedGameType(gameType)
    },

    onDifficultySelected(difficulty: AccuracyDifficulty) {
      this.selectedDifficulty = difficulty
      accuracyState.setLastSelectedDifficulty(difficulty)
    },

    onTimelessChanged(timeless: boolean) {
      this.timeless = timeless
      // Dont remove it from the list of animals if it is not selected
      // but add it if it is selected
      if (this.timeless)
        userSettings.addTimelessRegion(this.selectedRegion)
    },

    onRegionSelect(region: string) {
      this.selectedRegion = region
      this.showRegionSelect = false
      this.timeless = userSettings.isTimelessRegionAllowed(this.selectedRegion)
      accuracyState.setLastSelectedRegion(region)
    },
  }
})
</script>