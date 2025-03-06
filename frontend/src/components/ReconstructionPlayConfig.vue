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
      <span class="btn btn-action-neutral-1 rounded" @click="stop">Reset</span>
      <span class="btn btn-action-neutral-2 rounded" @click="stop">Change animals</span>
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
      this.$emit('animal-to-place-changed', val);
    },
    emitAnimalForHeatmapChanged(val: Animal) {
      this.$emit('animal-for-heatmap-changed', val);
    },
  }
})
</script>

<style scoped>
#select-animal-to-place {
  display: flex;
  flex-direction: column;
}
#reconstruction-play-config {
  max-width: fit-content;
  gap: 1rem;
  justify-content: space-between;
  flex-direction: column;
  align-items: start;
}

#title {
  align-self: center;
}

#select-options {
  flex: 1;
  text-align: start;
  display: grid;
  max-width: fit-content;
  gap: 1rem;
}

#action-buttons {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 1rem;
  column-gap: .5rem;
  row-gap: .25rem;
  flex-wrap: wrap-reverse;
}

#action-buttons > .btn {
  text-wrap: nowrap;
}

.dropdown {
  max-width: fit-content;
}
</style>