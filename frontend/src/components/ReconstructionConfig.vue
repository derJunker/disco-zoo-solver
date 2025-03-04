<template>
  <div class="config-board dock-bottom">
    <div id="title">Select Animals</div>
    <div class="first-row">
      <DropdownSelect :items="possibleRegions"
      @item-selected="(val : string | null) => selectedRegion = val?val : ''"/>
      <ToggleSelect title="Timeless" selected-color-class="timeless" @selected="onTimelessChange" :default-value="selectedTimeless"/>
    </div>
    <div id="animal-selection">
      <div>
        <div id="common-animals-title">
          Common:
        </div>
        <div id="common-animals">
          <span v-for="animal in commonAnimals" :key="animal.id" :class="'btn common-animal ' +
          (selectedAnimals.includes(animal)? 'highlighted' : '')"
                @click="onClickAnimal(animal)">
            {{animal.name.charAt(0)}}
          </span>
        </div>
      </div>
      <div>
        <div id="rare-animals-title">
          Rare:
        </div>
        <div id="rare-animals">
          <span v-for="animal in rareAnimals" :key="animal.id" :class="'btn rare-animal ' +
          (selectedAnimals.includes(animal)? 'highlighted' : '')"
                @click="onClickAnimal(animal)">
            {{animal.name.charAt(0)}}
          </span>
        </div>
      </div>
      <div id="epic-and-timeless">
        <div v-if="epicAnimal">
          <div id="epic-animals-title">
            Epic:
          </div>
          <div id="epic-animal-wrapper">
          <span :class="'btn epic-animal ' + (selectedAnimals.includes(epicAnimal)? 'highlighted' : '')"
                @click="onClickAnimal(epicAnimal)">
            {{epicAnimal.name.charAt(0)}}
          </span>
          </div>
        </div>
        <div v-if="selectedTimeless">
          <div id="timeless-animals-title">
            Timeless:
          </div>
          <div id="timeless-animal-wrapper">
          <span :class="'btn timeless-animal ' + (selectedAnimals.includes(timelessAnimal)? 'highlighted' : '')"
                @click="onClickAnimal(timelessAnimal)">
            {{timelessAnimal.name.charAt(0)}}
          </span>
          </div>
        </div>
      </div>
      <div>
        <div id="bux-animals-title">
          Discobux:
        </div>
        <div id="bux-animals">
          <span v-for="animal in timebux" :key="animal.id" :class="'btn bux-animal ' +
          (selectedAnimals.includes(animal)? 'highlighted' : '')"
                @click="onClickAnimal(animal)">
            {{animal.name.charAt(0)}}
          </span>
        </div>
      </div>
    </div>
    <div style="display: flex; justify-content: center;">
      <div class="btn btn-action-good" id="start-btn" @click="clicked_start">
        Start
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {Animal} from "@/types/Animal";
import {useAnimals} from "@/store/useAnimals";
import Dropdown from "@/components/Basic/Dropdown.vue";
import {useRegions} from "@/store/useRegions";
import ToggleSelect from "@/components/Basic/ToggleSelect.vue";

const animalStore = useAnimals();
const regionStore = useRegions();

export default defineComponent({
  name: 'reconstruction-config',
  components: {ToggleSelect, DropdownSelect: Dropdown},
  props: {

  },
  data() {
    return {
      possibleRegions: [] as string[],
      selectedRegion: '',

      selectedAnimals : [] as Animal[],
      commonAnimals : [] as Animal[],
      rareAnimals: [] as Animal[],
      epicAnimal: null as Animal | null,
      timebux: [] as Animal[],
      timelessAnimal: null as Animal | null,

      regionAnimals : [] as Animal[],

      selectedTimeless : false,
    };
  },
  async created() {
    this.possibleRegions = await regionStore.getAllRegions(false);
    await this.onRegionChange(this.possibleRegions[0]);
  },
  watch: {
    selectedRegion(newVal, oldVal) {
      if (newVal !== oldVal)
        this.onRegionChange(newVal);
    },
  },
  methods: {
    async onRegionChange(newRegion: string) {
      this.regionAnimals = await animalStore.getAnimalsOfRegion(newRegion)
      this.$emit('region-changed', newRegion);

      this.commonAnimals = this.regionAnimals.filter(animal => animal.rarity.toLowerCase() === 'common');
      this.rareAnimals = this.regionAnimals.filter(animal => animal.rarity.toLowerCase() === 'rare');
      this.epicAnimal = this.regionAnimals.filter(animal => animal.rarity.toLowerCase() === 'epic').pop()!;
      this.timelessAnimal = this.regionAnimals.filter(animal => animal.rarity.toLowerCase() === 'timeless').pop()!;
      this.timebux = this.regionAnimals.filter(animal => animal.rarity.toLowerCase() === 'bux');

      this.selectedAnimals = [];
      this.selectedTimeless = false;
      this.$emit('selected-animals-changed', this.selectedAnimals);
    },

    onTimelessChange(newVal: boolean) {
      this.selectedTimeless = newVal;
      if (!newVal)
        if (this.selectedAnimals.includes(this.timelessAnimal!)) {
          this.selectedAnimals = this.selectedAnimals.filter(animal => animal !== this.timelessAnimal);
          this.$emit('selected-animals-changed', this.selectedAnimals);
        }
    },

    onClickAnimal(animal: Animal) {
      if (this.selectedAnimals.includes(animal))
        this.selectedAnimals = this.selectedAnimals.filter(anim => anim !== animal);
      else {
        if (this.selectedAnimals.length >= 3)
          this.selectedAnimals.shift();
        this.selectedAnimals.push(animal);
      }
      this.$emit('selected-animals-changed', this.selectedAnimals);
    },

    clicked_start() {
      this.$emit('start', this.selectedAnimals);
    }
  }
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.first-row {
  display: flex;
  justify-content: space-between;
}

.config-board > * {
  margin-bottom: 1rem;
}
#title {
  font-size: var(--font-size-larger);
}

#common-animals, #rare-animals, #epic-animal-wrapper, #timeless-animal-wrapper, #bux-animals {
  padding-top: .2rem;
  display: flex;
  gap: .25rem;
  user-select: none;
}

#common-animals-title, #rare-animals-title, #epic-animals-title, #timeless-animals-title, #bux-animals-title {
  text-align: left;
}

.common-animal {
  background-color: var(--rarity-common);
}

.rare-animal {
  background-color: var(--rarity-rare);
}

.epic-animal {
  background-color: var(--rarity-epic);
}

.timeless-animal {
  background-color: var(--rarity-timeless);
}

.bux-animal {
  background-color: var(--rarity-bux);
}

#epic-and-timeless {
  display: flex;
  justify-content: space-between;
}

#start-btn {
  padding: 1rem 2rem 1rem 2rem;
  max-width: fit-content;
}
</style>
