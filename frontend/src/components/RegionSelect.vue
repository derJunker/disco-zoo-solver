<template>
 <div class="region-select rounded">
   <div v-for="region in regions" class="rounded" :key="region" @click="$emit('region-select', region)">
     {{region}}
   </div>
 </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useRegions} from "@/store/useRegions";

const regionStore = useRegions()

export default defineComponent({
  name: "RegionSelect",
  data() {
    return {
      regions: [] as string[]
    }
  },
  async created() {
    this.regions = await regionStore.getAllRegions()
  }
})
</script>

<style scoped>
.region-select {
  background-color: gray;
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
  padding: .3rem;
  min-width: 75%;
}
</style>