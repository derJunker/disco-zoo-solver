<template>
  <div class="accuracy-view">
    <div class="accuracy-content">
      <accuracy-config class="accuracy-config dock-bottom menu-bottom"
                       :selected-region="selectedRegion" :selected-game-type="selectedGameType" :timeless="timeless"
                       :selected-difficulty="selectedDifficulty"
                       @timeless-changed="onTimelessChanged"
                       @region-clicked="onRegionClicked" @game-type-selected="onGameTypeSelected"
                       @difficulty-selected="onDifficultySelected"/>
      <region-select v-if="showRegionSelect"  @region-select="onRegionSelect"
                      class="region-select dock-bottom dock-bottom-shadow menu-bottom" :any-option-available="true"/>
    </div>
    <menu-bar :on-first-button-click="onBack" :on-second-button-click="onPlay" second-button-name="play"
              first-button-name="back" first-color-class="color-action-neutral-1"
              second-color-class="color-action-neutral-2"/>
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
import {userSettingsState} from "@/store/useState";

const userSettings = userSettingsState()

export default defineComponent({
  name: "AccuracyView",
  components: {RegionSelect, AccuracyConfig, MenuBar},
  data() {
    return {
      selectedRegion: "farm" as string | null,
      selectedGameType: AccuracyGameType.SINGLE_CLICK as string,
      selectedDifficulty: AccuracyDifficulty.MEDIUM,
      showRegionSelect: false,
      timeless: userSettings.isTimelessRegionAllowed("farm")
    }
  },
  methods: {
    onBack() {
      router.push({name: 'home'})
    },
    onPlay() {
      router.push({name: 'accuracy-' + this.selectedGameType +'-play',
        params: {seed: this.generateSeed(), region:
          this.selectedRegion, difficulty: this.selectedDifficulty},
        query: {timeless: this.timeless + ""}})
    },

    onRegionClicked() {
      this.showRegionSelect = true
    },

    onGameTypeSelected(gameType: string) {
      this.selectedGameType = gameType
    },

    onDifficultySelected(difficulty: AccuracyDifficulty) {
      this.selectedDifficulty = difficulty
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
    },

    generateSeed() {
      return Math.floor(Math.random() * 1000000)
    }
  }
})
</script>