<template>
<div class="content">
  <div class="menus">
    <RegionSelect v-if="!$route.params.region" @region-select="onRegionSelect" class="dock-bottom"/>
    <AnimalSelect v-else-if="$route.params.region" @animal-select="onAnimalSelect"
                  :region="$route.params.region" class="dock-bottom"/>
  </div>
  <MenuBar :on-first-button-click="onBack" :on-second-button-click="onPlay" second-button-name="play"
           first-button-name="back" first-color-class="color-action-neutral-1"
           second-color-class="color-action-neutral-2"
  />
</div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import RegionSelect from "@/components/RegionSelect.vue";
import MenuBar from "@/components/MenuBar.vue";
import AnimalSelect from "@/components/AnimalSelect.vue";
import {Animal} from "@/types/Animal";
import router from "@/router";


export default defineComponent({
  name: "ReconstructView",
  components: {AnimalSelect, MenuBar, RegionSelect},
  data() {
    return {
      selectedAnimals: [] as Animal[]
    }
  },

  methods: {
    onRegionSelect(region: string) {
      router.push({name: "reconstruct-region", params: {region}})
    },

    onAnimalSelect(animals: Animal[]) {
      console.log("animal select")
      console.log(animals)
      this.selectedAnimals = animals;
    },

    onPlay() {
      console.log("play")
      console.log(this.selectedAnimals)
      if (this.selectedAnimals.length === 0) {
        return;
      }
      router.push({name: "reconstruct-play", query: {animals: this.selectedAnimals.map(animal => animal.name)}})
    },

    onBack() {
      console.log("back")
    },
  }
})
</script>

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