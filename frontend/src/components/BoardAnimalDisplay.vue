<template>
  <div id="board-animal-display">
    <div v-for="animal in animals" :key="animal.name" class="animal-display rounded dock-top"
         :style="'background-color: ' +
  regionColors.dark + ';'">
      <img id="animal-icon" :src="getAnimalPictureUrl(animal)" alt="animal" style="max-width: 50%;"/>
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
import {useAnimals} from "@/store/useAnimals";

const animalStore = useAnimals();

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
  },

  methods: {
    getAnimalPictureUrl(animal: Animal): string {
      return animalStore.getAnimalPictureUrl(animal);
    }
  }
})
</script>
<style scoped>
#board-animal-display {
  display: flex;
  gap: .5rem;
  justify-content: center;
  align-self: center;
  justify-items: center;
}

.animal-display {
  position: relative;
  display: grid;
  place-items: center;
  border: var(--border-small) solid rgba(0, 0, 0, var(--border-dark-opacity));
  aspect-ratio: 7/6;
  max-height: 7rem;
}

#animal-icon {
  align-self: center;
  margin-bottom: .75rem;
}

#animal-name {
  position: absolute;
  align-self: end;
  padding-bottom: .5rem;
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