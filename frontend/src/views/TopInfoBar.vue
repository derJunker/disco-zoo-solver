<template>
  <div class="top-info-bar btn-gradient" :style="getStyle()">
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
</style>

<script lang="ts">
import {defineComponent} from "vue";
import {getRegionColors} from "@/util/region-colors";

export default defineComponent({
  name: 'top-info-bar',
  props: {
    region: {
      type: String,
      required: false,
      default: null
    }
  },

  methods: {
    getStyle() {
      const regionColors = getRegionColors(this.region)
      return {
        backgroundColor: regionColors.primary,
      }
    }
  }
})
</script>