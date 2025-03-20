<template>
  <div class="accuracy-single-game-view">
    <div class="accuracy-single-game-content">
      <h1>
        Single Game
      </h1>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="info"/>
  </div>
</template>

<style scoped>
.accuracy-single-game-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-game-content {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import router from "@/router";

export default defineComponent({
  name: "AccuracyPlayView",
  components: {MenuBar},
  methods: {
    onBack() {
      router.push({name: 'accuracy'})
    }
  }
})
</script>