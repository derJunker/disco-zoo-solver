<template>
  <div class="home-view">
    <div class="home-view-content">
      <div id="home-text" class="wood-menu dock-top-shadow">
        <div class="heading-container wood-menu-group">
          <h1 class="rare btn btn-gradient">Disco Zoo Solver</h1>
        </div>
        <div class="wood-menu-group">
          <p>Welcome to the Disco Zoo Solver Website!</p>
          <p>This is the place to sharpen your disco-zoo rescue abilities and perfect your accuracy to the extreme
             ;).</p>
          <p>You can also use this website to find those pesky discobux and pets easier!</p>
          <p>If you're wondering how to play/use this site: <span tabindex="0"
                                                                  class="btn btn-gradient color-action-info"
                                                                  @click="onTutorialClick">
            Click Here</span></p>
        </div>
      </div>
      <transition name="overlay">
        <menu-overlay v-if="showMenuOverlay" class="menu-overlay dock-bottom menu-bottom dock-bottom-shadow"/>
        <play-overlay v-else-if="showPlayOverlay" class="play-overlay dock-bottom menu-bottom dock-bottom-shadow"/>
        <tutorial-overlay v-else-if="showTutorial" class="tutorial-overlay dock-bottom menu-bottom dock-bottom-shadow"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onMenuClick" first-color-class="color-action-neutral-1" first-button-name="Menu"
              :on-second-button-click="onPlayClick" second-color-class="color-action-neutral-2"
              second-button-name="Play"/>
  </div>


</template>

<style scoped>
#home-text {
  max-width: min(90%, 700px);
  margin-inline: auto;
  margin-top: 1rem;
}
span.btn {
  max-width: fit-content;
  display: inline;
  padding: 0.25rem .5rem;
  text-wrap: nowrap;
}
h1 {
  text-align: center;
  max-width: fit-content;
  padding: 1rem 1.5rem;
  margin-inline: auto;
  font-size: 1.5rem;
}
.heading-container {
  margin-inline: auto;
  margin-bottom: 1rem;
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
.menu-overlay, .play-overlay, .tutorial-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}

.menu-overlay {
  max-width: min(90%, 400px);
}

.play-overlay {
  max-width: min(90%, 400px);
}

.tutorial-overlay {
  max-width: min(95%, 900px);
}

</style>

<script lang="ts">
import MenuBar from "@/components/MenuBar.vue";
import {defineComponent} from "vue";
import MenuOverlay from "@/components/Overlays/MenuOverlay.vue";
import PlayOverlay from "@/components/Overlays/PlayOverlay.vue";
import TutorialOverlay from "@/components/Overlays/TutorialOverlay.vue";

export default defineComponent({
  name: 'HomeView',
  components: {TutorialOverlay, PlayOverlay, MenuOverlay, MenuBar},

  methods: {
    onMenuClick() {
      if (!this.showMenuOverlay) {
        this.showPlayOverlay = false;
        this.showTutorial = false;
      }
      this.showMenuOverlay = !this.showMenuOverlay;
    },

    onPlayClick() {
      if (!this.showPlayOverlay) {
        this.showMenuOverlay = false;
        this.showTutorial = false;
      }
      this.showPlayOverlay = !this.showPlayOverlay;
    },

    onTutorialClick() {
      if (!this.showTutorial) {
        this.showMenuOverlay = false;
        this.showPlayOverlay = false;
      }
      this.showTutorial = !this.showTutorial;
    }
  },

  data() {
    return {
      showMenuOverlay: false,
      showPlayOverlay: false,
      showTutorial: false
    }
  }
});
</script>