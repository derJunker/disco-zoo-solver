<template>
  <div class="reconstruct-play-view">
    <div class="reconstruct-content">
      <AnimalDisplay :animals="animals"/>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="onConfig" second-color-class="color-action-neutral-2"
              second-button-name="config"/>
  </div>
</template>

<style scoped>
.reconstruct-play-view {
  display: flex;
  flex-direction: column;
}

.reconstruct-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {Animal} from "@/types/Animal";
import AnimalDisplay from "@/components/AnimalDisplay.vue";
import {useState} from "@/store/useState";
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";


const state = useState()

export default defineComponent({
  name: "ReconstructPlayView",
  components: {MenuBar, AnimalDisplay},
  data() {
    return {
      animals: [] as Animal[]
    }
  },

  created() {
      this.animals = state.selectedAnimals
  },

  methods: {
    onBack() {
      router.push({name: "reconstruct-region", params: {region: state.selectedRegion}})
    },
    onConfig() {
      console.log("config")
    }
  }
})
</script>