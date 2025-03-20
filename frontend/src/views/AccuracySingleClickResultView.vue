<template>
  <div class="accuracy-single-click-result-view">
    <div class="accuracy-single-click-result-content">
      <h1>Accuracy Single Click Result</h1>
      <p>Here you can see the result of your accuracy single click game.</p>
    </div>
    <menu-bar :on-first-button-click="console.log" first-color-class="color-action-neutral-1" first-button-name="sth"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2" second-button-name="sth"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-result-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-click-result-content {
  position: relative;
  flex: 1;
  display: grid;
  place-items: center;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";

export default defineComponent({
  name: "AccuracySingleClickResultView",
  components: {MenuBar}
})
</script>