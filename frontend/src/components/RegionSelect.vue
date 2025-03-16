<template>
 <div class="region-select rounded border-light">
   <div v-for="region in regions" class="rounded btn" :key="region" @click="$emit('region-select', region)"
        :style="'background-color: ' + regionColors[region].primary">
     {{region}}
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
    this.regions = await regionStore.getAllRegions()
    for (const region of this.regions) {
      this.regionColors[region] = getRegionColors(region)
    }
  }
})
</script>

<style scoped>
.region-select {
  background-color: var(--wood-color-dark);
  display: grid;
  place-items: center;
  gap: .3rem;
  padding: 1rem 0;
  margin-inline: 1.5rem;
  grid-template-columns: repeat(auto-fit, minmax(20rem, 1fr));
  user-select: none;
}

.region-select > div {
  text-align: center;
  background-color: #707070;
  padding: .5rem;
  min-width: 75%;
}
</style>