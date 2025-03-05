<template>
  <div id="reconstruction-play-config" class="config-board dock-bottom">
    <div id="title">Reconstruct</div>
    <div id="select-options">
      <div id="select-animal-to-place">
        <div>Select Animal to Place:</div>
        <custom-dropdown :items="animals.concat(nothingAnimal())" :item-mapper="animalToString"
                         :default-value="nothingAnimal()"
                         class="dropdown" @item-selected="emitAnimalToPlaceChanged"/>
      </div>
      <div id="select-heatmap-focus">
        <div>Select Animal for Heatmap:</div>
        <custom-dropdown :items="animals" :item-mapper="animalToString" class="dropdown"
                         @item-selected="emitAnimalForHeatmapChanged"/>
      </div>
    </div>
    <div id="action-buttons">
      <span class="btn-action-neutral-2 rounded" @click="stop">Start</span>
      <span class="btn-action-neutral-2 rounded" @click="stop">Stop</span>
    </div>
  </div>
</template>
<script lang="ts">

import {defineComponent} from "vue";
import CustomDropdown from "@/components/Basic/DropdownSelect.vue";
import {Animal} from "@/types/Animal";
import {nothingAnimal} from "@/util/nothing-animal";

export default defineComponent({
  name: 'reconstruction-play-config',
  components: {CustomDropdown},
  props: {
    animals: {
      type: Array as () => Animal[],
      required: true
    }
  },
  methods: {
    nothingAnimal() {
      return nothingAnimal
    },
    animalToString(animal: Animal | null | undefined) {
      return animal?.name;
    },

    emitAnimalToPlaceChanged(val: Animal) {
      console.log("emitAnimalToPlaceChanged", val);
      this.$emit('animal-to-place-changed', val);
    },
    emitAnimalForHeatmapChanged(val: Animal) {
      console.log("emitAnimalForHeatmapChanged", val);
      this.$emit('animal-for-heatmap-changed', val);
    },
  }
})
</script>

<style scoped>
  #reconstruction-play-config {
    display: flex;
    flex-direction: column;
    align-items: start;
    padding-bottom: 2rem;
  }

  #title {
    align-self: center;
  }

  #select-options {
    flex: 1;
    text-align: start;
    display: grid;
    max-width: fit-content;
  }

  #action-buttons {
    display: flex;
    justify-content: space-between;
    width: 100%;
  }

  .dropdown {
    max-width: 50%;
  }
</style>