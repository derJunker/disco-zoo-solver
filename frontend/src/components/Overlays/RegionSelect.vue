<template>
 <div class="region-select wood-menu">
   <h1>Regions</h1>
   <div class="regions">
     <button v-for="region in regions" class="rounded btn btn-gradient" :key="region" @click="$emit('region-select',
     region)"
          :style="'background-color: ' + regionColors[region].primary">
       {{region}}
     </button>
   </div>
 </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useRegions} from "@/store/useRegions";
import {getRegionColors} from "@/util/region-colors";

const regionStore = useRegions()

export default defineComponent({
  name: "RegionSelect",
  props: {
    anyOptionAvailable: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      regions: [] as string[],
      regionColors: {} as  {[key: string]: {dark: string, light: string, primary: string}}
    }
  },
  async created() {
    this.regions = [...regionStore.hardcodedRegions]
    if (this.anyOptionAvailable)
      this.regions.unshift("Any")

    regionStore.getAllRegions().then(list => {
      if (list.length == 0) {
        return
      }
      if (JSON.stringify(regionStore.hardcodedRegions) !== JSON.stringify(list)) {
        if (this.anyOptionAvailable)
          list.unshift("Any")
        this.regions = list;
        for (const region of this.regions) {
          this.regionColors[region] = getRegionColors(region);
        }
      }
    })
    for (const region of this.regions) {
      this.regionColors[region] = getRegionColors(region)
    }

  },
})
</script>

<style scoped>
.regions {
  display: grid;
  grid-auto-flow: column;
  grid-template-rows: repeat(7, auto);  /* You can tweak this number */
  grid-auto-columns: 1fr;

  gap: 0.3rem;
  user-select: none;
  place-items: center;
}

.regions > button {
  text-align: center;
  padding: .5rem;
  min-width: 100%;
}

@media (max-aspect-ratio: 3/5) {
  .regions {
    grid-auto-flow: row;
    grid-template-columns: 1fr;
    grid-template-rows: none;
  }

  .regions > button {
    min-width: 90%;
  }
}


</style>