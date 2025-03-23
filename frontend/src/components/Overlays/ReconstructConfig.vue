<template>
  <div class="reconstruct-config wood-menu">
    <h1>Reconstruction - Config</h1>
    <div class="reconstruct-config-content">
      <div>
        <h2>
          Region:
        </h2>
        <div id="select-region-btn" class="btn btn-gradient" @click="$emit('region-clicked')"
             :style="selectedRegion ? ('background-color: ' + calcRegionColors(selectedRegion).primary) :
             'background-color: gray'">
          {{selectedRegion? selectedRegion : 'Select Region'}}
        </div>
      </div>
      <div class="toggle-select-container">
        <toggle-select title="timeless" selected-color-class="timeless" class="toggle-select"
                       @selected="(newVal:boolean) => $emit('timeless-changed', newVal)" :default-value="timeless"/>
      </div>
      <AnimalSelect v-if="selectedRegion"
                    :region="selectedRegion" :selected-animals="selectedAnimals" :timeless="timeless"
                    @animals-selected="(newVal:Animal[]) => $emit('animals-selected', newVal)"
                    class="animal-select"/>
    </div>
  </div>
</template>

<style scoped>
.reconstruct-config-content {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
#select-region-btn {
  min-width: 15rem;
  text-align: center;
}
.toggle-select-container {
  display: grid;
  place-items: end;
}
.animal-select {
  flex: 1;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import AnimalSelect from "@/components/Overlays/AnimalSelect.vue";
import {getRegionColors} from "@/util/region-colors";
import ToggleSelect from "@/components/Basic/ToggleSelect.vue";
import {Animal} from "@/types/Animal";

export default defineComponent({
  name: "ReconstructConfig",
  components: {ToggleSelect, AnimalSelect},
  props: {
    selectedRegion: {
      type: String,
      required: false
    },
    timeless: {
      type: Boolean,
      required: false
    },
    selectedAnimals: {
      type: Array as () => Animal[],
      required: false
    }
  },

  methods: {
    calcRegionColors() {
      return getRegionColors(this.selectedRegion!)
    },
  },
})
</script>