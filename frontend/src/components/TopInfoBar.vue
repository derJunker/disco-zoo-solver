<template>
  <div class="top-info-bar btn-gradient" :style="{backgroundColor: regionColors?.primary}">
    <div id="region">
      {{region}}
    </div>
    <slot/>
  </div>
</template>

<style scoped>

.top-info-bar {
  display: flex;
  justify-content: space-between;
  z-index: 10;
  padding: .5rem;

}

.top-info-bar > * {
  text-align: center;
  align-items: center;
  font-size: 1.3rem;
}

#region {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import {RegionColors} from "@/types/RegionColors";

export default defineComponent({
  name: 'top-info-bar',
  props: {
    regionColors: {
      type: Object as () => RegionColors,
      required: false,
      default: null
    }
  },
})
</script>