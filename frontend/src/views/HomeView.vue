<template>
  <div class="home-view">
    <div class="home-view-content">
      <div class="heading-container">
        <h1 class="rare btn-gradient btn btn-highlighted">Disco Zoo Solver</h1>
      </div>
      <transition name="overlay">
        <menu-overlay v-if="showMenuOverlay" class="menu-overlay dock-bottom menu-bottom"/>
        <play-overlay v-else-if="showPlayOverlay" class="play-overlay dock-bottom menu-bottom"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onMenuClick" first-color-class="color-action-neutral-1" first-button-name="Menu"
              :on-second-button-click="onPlayClick" second-color-class="color-action-neutral-2"
              second-button-name="Play"/>
  </div>


</template>

<style scoped>
h1 {
  text-align: center;
}
.heading-container {
  margin-top: 5rem;
  margin-inline: auto;
  max-width: min(90%, 600px);
  filter: drop-shadow(0 0 30px var(--animal-highlight-color));
}
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
import {useErrors} from "@/store/useErrors";

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