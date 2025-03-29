<template>
  <div class="about-view">
    <div class="about-view-content">
      <div id="about-menu" class="wood-menu">
        <h1>About</h1>
        <p>
          This is a simple site that assists you in playing Disco Zoo.
        </p>
        <p>This is just a passion project of mine and I am not affiliated with the game's creator. I just enjoy the
        game and community 😀!</p>
        <p>
          It is a work in progress and there is no guarantee that my algorithm 100% accurate. Feedback and ideas are always
          appreciated!
          <br>
          If you find something which you think is a bug you can write me on discord(@derjunker) /e-mail any time!
        </p>
        <p>If you stumbled upon this site and have not yet joined the discord,
          <a style="color: #00A6F0; text-decoration: underline;" href="https://discord.gg/VwxRrkgX" target="_blank">
          consider joining</a> 😇 .</p>
        <p>
          I built this site in my free time and pay for the hosting myself. If you would like to support me, consider
          buying me a coffee :).
        </p>
        <form action="https://www.paypal.com/donate" method="post" target="_blank">
          <input type="hidden" name="hosted_button_id" value="XH72PGMV2ML48" />
          <input type="image" src="/btn_donate_SM.gif" name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button" />
        </form>
      </div>
      <transition name="overlay">
        <menu-overlay v-if="showMenu" class="menu-overlay dock-bottom menu-bottom dock-bottom-shadow"/>
        <play-overlay v-else-if="showPlay" class="play-overlay dock-bottom menu-bottom dock-bottom-shadow"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onMenuClick" first-color-class="color-action-neutral-1" first-button-name="menu"
              :on-second-button-click="onPlayClick" second-color-class="color-action-neutral-2"
              second-button-name="play"></menu-bar>
  </div>
</template>
<style scoped>
.about-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.about-view-content {
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
  max-width: min(90%, 400px);
}

.play-overlay {
  max-width: min(90%, 400px);
}

#about-menu {
  margin-top: 2rem;
  margin-inline: auto;
  max-width: min(95%, 600px);
}

</style>

<script lang="ts">
import { defineComponent } from 'vue';
import MenuBar from "@/components/MenuBar.vue";
import MenuOverlay from "@/components/Overlays/MenuOverlay.vue";
import PlayOverlay from "@/components/Overlays/PlayOverlay.vue";

export default defineComponent({
  name: 'AboutView',
  components: {PlayOverlay, MenuOverlay, MenuBar},
  data() {
    return {
      showMenu: false,
      showPlay: false
    }
  },
  methods: {
    onMenuClick() {
      if (!this.showMenu) {
        this.showPlay = false;
      }
      this.showMenu = !this.showMenu;
    },

    onPlayClick() {
      if (!this.showPlay) {
        this.showMenu = false;
      }
      this.showPlay = !this.showPlay;
    },
  }
});
</script>
