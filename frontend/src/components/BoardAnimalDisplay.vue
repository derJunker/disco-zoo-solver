<template>
  <div id="animal-container">
    <div v-for="animal in animals" :key="animal.name" class="animal-display rounded dock-top"
         :style="'background-color: ' +
  regionColors.dark + ';'">
      <div id="animal-icon">
        ani
      </div>
      <div id="animal-name">
        {{animal.name}}
      </div>
      <div id="reveal-trackers">
        <div class="tracker rounded"></div>
        <div class="tracker rounded"></div>
        <div class="tracker rounded"></div>
      </div>
    </div>

  </div>
</template>
<script lang="ts">
import {Animal} from "@/types/Animal";
import {getRegionColors} from "@/util/region-colors";
import {defineComponent} from "vue";

export default defineComponent ({
  name: 'BoardAnimalDisplay',
  props: {
    region: {
      type: String,
      required: true
    },
    animals: {
      type: Array as () => Animal[],
      required: true
    }
  },

  data() {
    return {
      regionColors: {primary: '', dark: '', light: ''}
    }
  },

  created() {
    this.regionColors = getRegionColors(this.region);
  },

  watch: {
    region(newVal, oldVal) {
      if (newVal !== oldVal)
        this.regionColors = getRegionColors(this.region);
    },
  }
})
</script>
<style scoped>
#animal-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(5rem, 1fr));
  gap: .5rem;
  justify-self: center;
  justify-items: center;
  max-width: 100%;
  height: 95%;
}

.animal-display {
  position: relative;
  display: grid;
  grid-template-rows: subgrid;
  container-type: inline-size;
  border: var(--border-small) solid rgba(0, 0, 0, var(--border-dark-opacity));
  aspect-ratio: 1;
}

#animal-icon {
  grid-row: 2;
  align-self: center;
}

#animal-name {
  grid-row: 3;
  align-self: end;
  height: 50%;
  padding-bottom: .25rem;
}

#reveal-trackers {
  position: absolute;
  bottom: -.5rem;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  gap: .1rem;
}

.tracker {
  background-color: black;
  border: white solid .1rem;
  aspect-ratio: 1;
  min-width: .75rem;
}
</style>