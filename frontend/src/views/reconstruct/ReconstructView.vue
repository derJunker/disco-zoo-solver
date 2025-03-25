<template>
<div class="reconstruction-view">
  <div class="reconstruction-view-content">
    <reconstruct-config :selected-region="selectedRegion" :timeless="timeless"
                        :selected-animals="selectedAnimals"
                        @region-clicked="onRegionClicked" @timeless-changed="onTimelessChanged"
                        @animals-selected="onAnimalsSelected"
                        class="menu-bottom dock-bottom reconstruct-config"/>
    <RegionSelect v-if="showRegionSelect" @region-select="onRegionSelect"
                  class="region-select dock-bottom dock-bottom-shadow menu-bottom"/>
  </div>
  <menu-bar :on-first-button-click="onBack" :on-second-button-click="onPlay" second-button-name="play"
           first-button-name="back" first-color-class="color-action-neutral-1"
           second-color-class="color-action-neutral-2"
  />
</div>
</template>

<style scoped>
.reconstruction-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.reconstruction-view-content {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: end;
}

.reconstruct-config {
  max-width: min(90%, 500px);
}

.region-select {
  max-width: min(90%, 700px);
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import RegionSelect from "@/components/Overlays/RegionSelect.vue";
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import ReconstructConfig from "@/components/Overlays/ReconstructConfig.vue";
import {Animal} from "@/types/Animal";
import {useReconstructState, userSettingsState} from "@/store/useState";

const userSettings = userSettingsState()
const reconstructState = useReconstructState()

export default defineComponent({
  name: "ReconstructView",
  components: {ReconstructConfig, MenuBar, RegionSelect},
  data() {
    return {
      selectedRegion: reconstructState.getLastSelectedRegion || "farm" as string | null,
      timeless: userSettings.isTimelessRegionAllowed(reconstructState.getLastSelectedRegion || "farm"),
      selectedAnimals: [] as Animal[],
      showRegionSelect: false,
    }
  },

  methods: {

    onTimelessChanged(newVal: boolean) {
      this.timeless = newVal
      if (!this.timeless) {
        const indexOfTimeless = this.selectedAnimals.findIndex(animal => animal.rarity.toUpperCase() === "TIMELESS")
        if (indexOfTimeless !== -1) {
          this.selectedAnimals.splice(indexOfTimeless, 1)
        }

        userSettings.removeTimelessRegion(this.selectedRegion)
      } else {
        userSettings.addTimelessRegion(this.selectedRegion)
      }
    },

    onRegionSelect(region: string) {
      this.selectedRegion = region.toLowerCase()
      this.showRegionSelect = false
      this.selectedAnimals = []
      this.timeless = userSettings.isTimelessRegionAllowed(this.selectedRegion)
      reconstructState.setLastSelectedRegion(region)
    },

    onRegionClicked() {
      this.showRegionSelect = true
    },

    onAnimalsSelected(animals: Animal[]) {
      this.selectedAnimals = animals
    },

    onPlay() {
      if (this.selectedAnimals.length === 0 || this.selectedRegion === null) {
        return;
      }
      router.push({name: "reconstruct-play", params: {region: this.selectedRegion!}, query: {animals:
          this.selectedAnimals.map(animal => animal.name).join(",")}})
    },

    onBack() {
      router.push({name: "home"})
    },
  }
})
</script>

