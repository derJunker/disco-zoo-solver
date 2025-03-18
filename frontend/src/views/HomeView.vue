<template>
  <div class="home-view">
    <div class="home-view-content">
      <h1>Disco Zoo Solver</h1>
      <router-link to="/reconstruct">Reconstruct</router-link>
      <router-link to="/about">About</router-link>
    </div>
    <menu-bar :on-first-button-click="console.log" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="config"/>
  </div>


</template>

<style scoped>
.home-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.home-view-content {
  flex: 1;
}
</style>

<script lang="ts">
import MenuBar from "@/components/MenuBar.vue";
import {defineComponent} from "vue";

export default defineComponent({
  name: 'HomeView',
  components: {MenuBar}
});
</script>