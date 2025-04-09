<template>
<div class="reconstruction-view footer-view">
  <div class="reconstruction-view-content footer-view-content">
    <transition name="overlay">
      <reconstruct-config v-if="!showRegionSelect && showConfig" :selected-region="selectedRegion" :timeless="timeless"
                          :selected-animals="selectedAnimals" :selected-pet="selectedPet"
                          @region-clicked="onRegionClicked" @timeless-changed="onTimelessChanged"
                          @animals-selected="onAnimalsSelected"
                          @pet-selected="onPetSelected"
                          class="menu-bottom dock-bottom reconstruct-config"/>
      <RegionSelect v-else-if="showRegionSelect" @region-select="onRegionSelect"
                    class="region-select dock-bottom dock-bottom-shadow menu-bottom"/>
    </transition>
  </div>
  <menu-bar :on-first-button-click="onBack" :on-second-button-click="showRegionSelect? onCloseRegionSelect : onPlay"
            :second-button-name="showRegionSelect? 'close' : 'play'"
           first-button-name="back" first-color-class="color-action-neutral-1"
           :second-color-class="showRegionSelect? 'color-action-bad' : 'color-action-neutral-2'"
  />
</div>
</template>

<style scoped>
.reconstruction-view-content {
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
import {defineComponent, ref} from "vue";
import RegionSelect from "@/components/Overlays/RegionSelect.vue";
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";
import ReconstructConfig from "@/components/Overlays/ReconstructConfig.vue";
import {Animal} from "@/types/Animal";
import {useReconstructState, userSettingsState} from "@/store/useState";
import {useErrors} from "@/store/useErrors";

const userSettings = userSettingsState()
const reconstructState = useReconstructState()
const errorState = useErrors()

export default defineComponent({
  name: "ReconstructView",
  components: {ReconstructConfig, MenuBar, RegionSelect},
  data() {
    return {
      selectedRegion: reconstructState.getLastSelectedRegion || "farm" as string | null,
      timeless: userSettings.isTimelessRegionAllowed(reconstructState.getLastSelectedRegion || "farm"),
      selectedAnimals: [] as Animal[],
      showRegionSelect: false,
      selectedPet: null as Animal | null
    }
  },

  setup() {
    const showConfig = ref(false)
    return {showConfig}
  },
  mounted() {
    this.showConfig = true;
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
      const rareCase = (this.selectedPet && animals.length == 2 && animals.every(animal =>
          ["zebra", "hippo"].includes(animal.name.toLowerCase())) && this.selectedPet.name.toLowerCase() === "hamster")
      if (animals.length == 4 || (animals.length == 3 && this.selectedPet) || rareCase) {
        animals.shift()
        if (rareCase)
          errorState.addError("Congrats! You found the 1 impossible assortment of animals and pets! Here a cookie 🍪")
      }
      this.selectedAnimals = animals
    },

    onPetSelected(pet: Animal | null) {
      this.selectedPet = pet
      const rareCase = (pet && this.selectedAnimals.length == 2 && this.selectedAnimals.every(animal =>
          ["zebra", "hippo"].includes(animal.name.toLowerCase())) && pet.name.toLowerCase() === "hamster")
      if (pet && (this.selectedAnimals.length > 2 || rareCase)) {
        this.selectedAnimals.shift()
        if (rareCase)
          errorState.addError("Congrats! You found the 1 impossible assortment of animals and pets! Here a cookie 🍪")
      }
    },

    onPlay() {
      if ((this.selectedAnimals.length === 0 && this.selectedPet === null) || this.selectedRegion === null) {
        return;
      }
      router.push({name: "reconstruct-play", params: {region: this.selectedRegion!}, query: {animals:
          this.selectedAnimals.map(animal => animal.name).join(","), pet: this.selectedPet?.name}})
    },

    onBack() {
      router.push({name: "home"})
    },

    onCloseRegionSelect() {
      this.showRegionSelect = false
    }
  }
})
</script>

