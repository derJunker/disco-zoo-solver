<template>
<div class="content">
  {{updateRegion($route.params.region)}}
  <div class="menus">
    <RegionSelect v-if="!$route.params.region" @region-select="onRegionSelect" class="dock-bottom"/>
    <AnimalSelect v-else @animal-select="onAnimalSelect"
                  :region="$route.params.region" class="dock-bottom"/>
  </div>
  <menu-bar :on-first-button-click="onBack" :on-second-button-click="onPlay" second-button-name="play"
           first-button-name="back" first-color-class="color-action-neutral-1"
           second-color-class="color-action-neutral-2"
  />
</div>
</template>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.menus {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: end;
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
      console.log("back")
    },

    // Function is a complete hack, bc for some reason i can't access $route.params in the script
    // so i call it in the template to update the region and then return an empty string 👍
    updateRegion(region: string) {
      console.log("update region", region)
      state.selectedRegion = region
      return ""
    }
  }
})
</script>

