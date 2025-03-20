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
      <div>
        <h2>
          Gametype:
        </h2>
        <div class="game-types-container">
          <div id="single-click-game-type" class="btn btn-gradient color-action-neutral-1" :class="selectedGameType
          === gameType.SINGLE_CLICK ? 'animal-highlighted' : ''"
               @click="$emit('game-type-selected', gameType.SINGLE_CLICK)">
            Single Click
          </div>
          <div id="single-game-game-type" class="btn btn-gradient color-action-neutral-3" :class="selectedGameType
          === gameType.SINGLE_GAME ? 'animal-highlighted' : ''"
               @click="$emit('game-type-selected', gameType.SINGLE_GAME)">
            Single Game
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
  row-gap: .2rem;
  column-gap: 1rem;
}
#select-region-btn {
  min-width: 15rem;
}
</style>


<script lang="ts">
import {defineComponent} from 'vue'
import {getRegionColors} from "@/util/region-colors";
import {AccuracyGameType} from "@/types/AccuracyGameType";

export default defineComponent({
  name: "AccuracyConfig",
  props: {
    selectedRegion: {
      type: String,
      required: true
    },
    selectedGameType: {
      type: String,
      required: false
    }
  },

  methods: {
    calcRegionColors(region: string) {
      return getRegionColors(region)
    }
  },

  data() {
    return {
      gameType: AccuracyGameType
    }
  }
})
</script>