<template>
 <div class="region-select wood-menu">
   <h1>Regions</h1>
   <div class="regions">
     <div v-for="region in regions" class="rounded btn btn-gradient" :key="region" @click="$emit('region-select', region)"
          :style="'background-color: ' + regionColors[region].primary">
       {{region}}
     </div>
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
    this.regions = regionStore.hardcodedRegions
    if (this.anyOptionAvailable)
      this.regions.unshift("Any")

    regionStore.getAllRegions().then(list => {
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
  place-items: center;
  gap: .3rem;
  grid-template-columns: repeat(auto-fit, minmax(20rem, 1fr));
  user-select: none;
}

.regions > div {
  text-align: center;
  padding: .5rem;
  min-width: 75%;
}
</style>