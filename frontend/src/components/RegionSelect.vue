<template>
 <div class="region-select wood-menu">
   <h3>Regions</h3>
   <div class="regions">
     <div v-for="region in regions" class="rounded btn" :key="region" @click="$emit('region-select', region)"
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
  data() {
    return {
      regions: [] as string[],
      regionColors: {} as  {[key: string]: {dark: string, light: string, primary: string}}
    }
  },
  async created() {
    this.regions = regionStore.hardcodedRegions
    regionStore.getAllRegions().then(list => {
      if (JSON.stringify(this.regions) !== JSON.stringify(list)) {
        this.regions = list;
        for (const region of this.regions) {
          this.regionColors[region] = getRegionColors(region);
        }
      }
    })
    console.log(this.regions)
    for (const region of this.regions) {
      this.regionColors[region] = getRegionColors(region)
    }

  }
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