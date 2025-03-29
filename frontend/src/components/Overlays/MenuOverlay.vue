<template>
  <div class="menu-overlay wood-menu">
    <h1>Menu</h1>
    <div class="menu-buttons">
      <button class="btn btn-gradient color-action-info" @click="clickedHome()">Home</button>
      <button class="btn btn-gradient color-action-neutral-3" @click="clickedAbout()">About</button>
<!--      <button class="btn btn-gradient color-action-neutral-2" @click="clickedSettings()">Settings</button>-->
      <!--      <button class="btn btn-gradient color-action-neutral-1" @click="clickedStats()">Stats</button>-->
    </div>
  </div>
</template>

<style scoped>
.menu-buttons {
  display: grid;
  grid-template-columns: repeat(auto-fit , minmax(7rem, 1fr));
  column-gap: .5rem;
  row-gap: .3rem;
}
.btn {
  padding: .7rem 1rem;
}
</style>

<script lang="ts">
import {defineComponent} from "vue";
import router from "@/router";

export default defineComponent({
  name: 'menu-overlay',
  methods: {
    clickedSettings() {
      router.push({name: 'settings'});
    },
    clickedAbout() {
      router.push({name: 'about'})
    },
    clickedStats() {
      router.push({name: 'stats'})
    },
    clickedHome() {
      router.push({name: 'home'})
    }
  }
})
</script>