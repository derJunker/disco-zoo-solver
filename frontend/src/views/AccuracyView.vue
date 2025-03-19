<template>
  <div class="accuracy-view">
    <div class="accuracy-content">
      <accuracy-config class="accuracy-config dock-bottom menu-bottom"
                       :selected-region="selectedRegion" :selected-game-type="selectedGameType"
                       @region-clicked="onRegionClicked" @game-type-selected="onGameTypeSelected"/>
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


</style>

<script lang="ts">
import {defineComponent} from "vue";
import MenuBar from "@/components/MenuBar.vue";
import AccuracyConfig from "@/components/Overlays/AccuracyConfig.vue";
import router from "@/router";

export default defineComponent({
  name: "AccuracyView",
  components: {AccuracyConfig, MenuBar},
  data() {
    return {
      selectedRegion: "farm" as string | null,
      selectedGameType: "single-click" as string | null,
    }
  },
  methods: {
    onBack() {
      router.push({name: 'home'})
    },
    onPlay() {
      console.log()
    },

    onRegionClicked() {
      console.log("region clicked")
    },

    onGameTypeSelected(gameType: string) {
      this.selectedGameType = gameType
    }
  }
})
</script>