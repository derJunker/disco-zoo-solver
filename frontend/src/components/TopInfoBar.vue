<template>
  <div class="top-info-bar btn-gradient" :style="{backgroundColor: regionColors?.primary, borderColor:
  regionColors?.dark}">
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
  border-bottom: 2px solid;
  position: relative;
}
.top-info-bar::before {
  content: '';
  display: block;
  left: 0;
  bottom: 0;
  z-index: 11;
  width: 100%;
  height: 2px;
  position: absolute;
  background-color: rgba(255, 255, 255, 0.3);
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