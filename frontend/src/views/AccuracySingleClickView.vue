<template>
  <div class="accuracy-single-click-view">
    {{setRouteValuesToVars($route.params.seed, $route.params.region)}}
    <div class="accuracy-single-click-content">
      <h1>
        Single Click {{seed}} {{region}}
      </h1>
    </div>
    <menu-bar :on-first-button-click="onBack" first-color-class="color-action-neutral-1" first-button-name="back"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="info"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.accuracy-single-click-content {
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
  data() {
    return {
      gameRound: 0,
      seed: null as number | null,
      region: null as string | null,
    }
  },
  methods: {
    onBack() {
      router.push({name: 'accuracy'})
    },

    setRouteValuesToVars(seed: number, region: string) {
      this.seed = seed
      this.region = region
      return ""
    }
  },
})
</script>