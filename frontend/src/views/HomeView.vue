<template>
  <div class="home-view">
    <div class="home-view-content">
      <h1>Disco Zoo Solver</h1>
      <menu-overlay v-if="showMenuOverlay" class="menu-overlay dock-bottom wood-menu"/>
      <play-overlay v-if="showPlayOverlay" class="play-overlay dock-bottom wood-menu"/>
    </div>
    <menu-bar :on-first-button-click="onMenuClick" first-color-class="color-action-neutral-1" first-button-name="Menu"
              :on-second-button-click="onPlayClick" second-color-class="color-action-neutral-2"
              second-button-name="Play"/>
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
  position: relative;
}
.menu-overlay, .play-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}

.menu-overlay {
  max-width: min(70%, 400px);
}

.play-overlay {
  max-width: min(90%, 400px);
}
</style>

<script lang="ts">
import MenuBar from "@/components/MenuBar.vue";
import {defineComponent} from "vue";
import MenuOverlay from "@/components/Overlays/MenuOverlay.vue";
import PlayOverlay from "@/components/Overlays/PlayOverlay.vue";

export default defineComponent({
  name: 'HomeView',
  components: {PlayOverlay, MenuOverlay, MenuBar},

  methods: {
    onMenuClick() {
      if (!this.showMenuOverlay)
        this.showPlayOverlay = false;
      this.showMenuOverlay = !this.showMenuOverlay;
    },

    onPlayClick() {
      if (!this.showPlayOverlay)
        this.showMenuOverlay = false;
      this.showPlayOverlay = !this.showPlayOverlay;
    }
  },

  data() {
    return {
      showMenuOverlay: false,
      showPlayOverlay: true
    }
  }
});
</script>