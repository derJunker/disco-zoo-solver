<template>
  <div>
    <div id="reconstruct-view-content">
      <div id="reconstruction-board" class="dock-bottom dock-top border-small">

      </div>
      <reconstruction-config id="animal-selection"/>
    </div>
  </div>
</template>
<script setup lang="ts">
import ReconstructionConfig from "@/components/ReconstructionConfig.vue";
</script>

<style scoped>
#reconstruct-view-content {
  display: grid;
  grid-template-columns: 5fr 7fr 5fr;
  height: 100%;
}

#reconstruction-board {
  grid-column: 2;
  background: var(--farm-light);
  height: 100%;
}

#animal-selection {
  margin-right: 2rem;
  margin-left: 2rem;
  height: 55%;
  align-self: end;
}
</style>