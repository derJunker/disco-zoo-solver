<template>
  <div class="settings-view footer-view">
    <div class="footer-view-content">
      <transition name="overlay">
        <div v-if="showSettings" id="settings-menu" class="wood-menu menu-bottom dock-bottom dock-bottom-shadow">
          <h1>Settings</h1>
          <labled-check-box input-id="show-perc" label="Show probabilities in Heatmap" :checked="showPercentages"
                            @checked="onShowPercentagesClicked"/>
        </div>
        <menu-overlay v-else-if="!showSettings && showMenuOverlay"
                      class="menu-overlay dock-bottom menu-bottom dock-bottom-shadow"
                      ref="menuRef" @clicked-settings="onMenuClick"/>
        <play-overlay v-else-if="!showSettings && showPlayOverlay" class="play-overlay dock-bottom menu-bottom dock-bottom-shadow"
                      ref="playRef"/>
      </transition>
    </div>
    <menu-bar :on-first-button-click="onMenuClick"
              :first-color-class="!showMenuOverlay ? 'color-action-neutral-1' : 'color-action-bad'"
              :first-button-name="!showMenuOverlay?'Menu':'Close'"
              :on-second-button-click="onPlayClick"
              :second-color-class="(!showPlayOverlay) ? 'color-action-neutral-2' : 'color-action-bad'"
              :second-button-name="(!showPlayOverlay) ? 'Play' : 'Close'" ref="menuBar"/>
  </div>
</template>

<style scoped>
.footer-view-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
#settings-menu {
  max-width: min(90%, 600px);
  min-height: 70%;
}

.menu-overlay {
  max-width: min(90%, 400px);
}

.play-overlay {
  max-width: min(90%, 400px);
}

.menu-overlay, .play-overlay, #settings-menu {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}
</style>

<script lang="ts">
import {defineComponent, ref} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import MenuOverlay from "@/components/Overlays/MenuOverlay.vue";
import PlayOverlay from "@/components/Overlays/PlayOverlay.vue";
import LabledCheckBox from "@/components/Basic/LabledCheckBox.vue";
import {useSettings} from "@/store/useSettings";

const settings = useSettings()

export default defineComponent({
  name: "SettingsView",
  components: {LabledCheckBox, PlayOverlay, MenuOverlay, MenuBar},
  data() {
    return {
      showMenuOverlay: false,
      showPlayOverlay: false,

      showPercentages: settings.showPercentages
    }
  },
  setup() {
    const showSettings = ref(false)
    return {showSettings}
  },
  mounted() {
    this.showSettings = true;
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  },

  watch: {
    showMenuOverlay: {
      handler(newVal) {
        if (!newVal && !this.showPlayOverlay) {
          this.showSettings = true;
        } else if (newVal) {
          this.showSettings = false;
        }
      },
      immediate: true,
    },
    showPlayOverlay: {
      handler(newVal) {
        if (!newVal && !this.showMenuOverlay) {
          this.showSettings = true;
        } else if (newVal) {
          this.showSettings = false;
        }
      },
      immediate: true,
    }
  },

  methods: {
    onShowPercentagesClicked(val: boolean) {
      this.showPercentages = val
      settings.setShowPercentages(val)
    },

    onMenuClick() {
      if (!this.showMenuOverlay) {
        this.showPlayOverlay = false;
      }
      this.showMenuOverlay = !this.showMenuOverlay;
    },

    handleClickOutside(event: any) {
      if (this.showMenuOverlay
          && !(this.$refs.menuRef as any).$el.contains(event.target)
          && !(this.$refs.menuBar as any).$el.contains(event.target)) {
        this.showMenuOverlay = false
      }
      if (this.showPlayOverlay
          && !(this.$refs.playRef as any).$el.contains(event.target)
          && !(this.$refs.menuBar as any).$el.contains(event.target)) {
        this.showPlayOverlay = false
      }

    },

    onPlayClick() {
      if (this.showPlayOverlay) {
        this.showPlayOverlay = false;
      } else {
        this.showPlayOverlay = true;
        this.showMenuOverlay = false;
      }
    },
  }
})
</script>