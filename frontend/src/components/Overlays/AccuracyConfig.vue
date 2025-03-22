<template>
 <div class="accuracy-config wood-menu">
    <h1>Accuracy - Config</h1>
    <div class="accuracy-config-options">
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
      <div>
        <h2>
          Gametype:
        </h2>
        <div class="game-types-container">
          <div v-for="gameType in possibleGameTypes" :key="gameType" class="btn btn-gradient game-type"
               :class="selectedGameType === gameType ? 'color-action-neutral-2 btn-highlighted' : 'color-action-disabled'"
               @click="$emit('game-type-selected', gameType)">
            {{gameType.replace(/-/g, ' ')}}
          </div>
        </div>
      </div>
    </div>
 </div>
</template>

<style scoped>
.btn {
  text-align: center;
}
.accuracy-config-options {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-between;
  row-gap: 1rem;
}
.game-types-container {
  display: flex;
  row-gap: .4rem;
  flex-wrap: wrap;
  column-gap: 1rem;
}
#select-region-btn {
  min-width: 15rem;
}
.toggle-select-container {
  display: grid;
  place-items: end;
}
.game-type {
  padding: .6rem;
}
</style>


<script lang="ts">
import {defineComponent} from 'vue'
import {getRegionColors} from "@/util/region-colors";
import {AccuracyGameType} from "@/types/AccuracyGameType";
import ToggleSelect from "@/components/Basic/ToggleSelect.vue";

export default defineComponent({
  name: "AccuracyConfig",
  components: {ToggleSelect},
  props: {
    selectedRegion: {
      type: String,
      required: true
    },
    selectedGameType: {
      type: String,
      required: true
    },
    timeless: {
      type: Boolean,
      required: true
    }
  },

  methods: {
    calcRegionColors(region: string) {
      return getRegionColors(region)
    },
  },

  data() {
    return {
      gameType: AccuracyGameType,
      possibleGameTypes: Object.values(AccuracyGameType)
    }
  },
})
</script>