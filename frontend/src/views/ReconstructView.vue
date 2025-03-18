<template>
<div class="reconstruction-view">
  {{updateRegion($route.params.region)}}
  <div class="menus">
    <RegionSelect v-if="!$route.params.region" @region-select="onRegionSelect" class="region-select dock-bottom dock-bottom-shadow"/>
    <AnimalSelect v-else
                  :region="$route.params.region" class="dock-bottom dock-bottom-shadow animal-select"/>
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
.menus {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: end;
}

.animal-select, .region-select {
  position: absolute;
  bottom: 0;
  margin-inline: auto;
  left: 0;
  right: 0;
  z-index: 1;
}

.animal-select {
  max-width: min(90%, 500px);
}

.region-select {
  max-width: min(90%, 700px);

}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import RegionSelect from "@/components/RegionSelect.vue";
import MenuBar from "@/components/MenuBar.vue";
import AnimalSelect from "@/components/AnimalSelect.vue";
import router from "@/router";
import {useState} from "@/store/useState";

const state = useState()

export default defineComponent({
  name: "ReconstructView",
  components: {AnimalSelect, MenuBar, RegionSelect},

  methods: {
    onRegionSelect(region: string) {
      region = region.toLowerCase()
      this.updateRegion(region)
      router.push({name: "reconstruct-region", params: {region}})
    },

    onPlay() {
      if (state.selectedAnimals.length === 0) {
        return;
      }
      router.push({name: "reconstruct-play"})
    },

    onBack() {
      if (state.selectedRegion) {
        router.push({name: "reconstruct"})
        state.selectedRegion = null
      }
      else
        router.push({name: "home"})
    },

    // Function is a complete hack, bc for some reason i can't access $route.params in the script
    // so i call it in the template to update the region and then return an empty string 👍
    updateRegion(region: string) {
      state.selectedRegion = region
      return ""
    }
  }
})
</script>

