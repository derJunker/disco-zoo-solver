<template>
  <div class="tutorial-overlay wood-menu">
    <h1> Tutorial </h1>
    <div class="content-wrapper">
      <button class="btn nav-btn nav-btn-left" @click="showIndex = loopIndex(showIndex-1)">{{"<"}}</button>
        <div class="wood-menu-group">
          <transition name="slide-fade">
            <div class="content" v-if="showIndex == 0">
              <h3>Heatmap:</h3>
              <div id="koala-heatmap">
                <img alt="koala heatmap" src="/koala_heatmap.jpg"/>
                <p style="font-size: .7rem; line-height: 1rem; text-align: center">Heatmap of a Koala</p>
              </div>
              <p>The heatmap shows you the likelihood for each square to contain the animal you're looking for.</p>
              <p>Even when 2 squares have the same likelihood to contain an animal, it does not necessarily mean that both
                 are equally as good to click. For clarity a white border is added to the best squares to click.</p>
            </div>
            <div class="content" v-else-if="showIndex == 1">
              <h3>Reconstruct:</h3>
              <p>Use this mode to quickly locate animals, pets, or Discobux with higher accuracy.</p>
              <p>If you're unsure how to find certain animals in Disco Zoo, open Reconstruct, select your region and animals, then use the config to choose which animal's heatmap you want to display.
              This way you can emulate the disco-zoo game and see which squares are the best to click!</p>
              <p>It also helps you gain insights into the best clicking patterns for different animals, sharpening your strategy over time.</p>

            </div>
            <div class="content" v-else-if="showIndex == 2">
              <h3>Accuracy:</h3>
              <p>These challenges are designed to put your skills to the test.</p>
              <p>Increasing the difficulty raises the average number of animals that will appear.</p>

              <h4>Single Click</h4>
              <p>You'll play through multiple rounds, aiming to make the best possible click for the animal you need
                 to find (highlighted in white).</p>
              <p>Afterward, you'll see your accuracy and have the chance to review each game to learn from any mistakes.</p>

              <h4>Streak</h4>
              <p>In this mode, you can keep playing as long as you continue clicking correctly.</p>
              <p>The goal is to get as many correct in a row as possible — good luck!</p>
            </div>
          </transition>
        </div>
      <button class="btn nav-btn nav-btn-right" @click="showIndex = loopIndex(showIndex+1)">{{">"}}</button>
    </div>
  </div>
</template>

<style scoped>

.tutorial-overlay {
  height: fit-content;
  display: grid;
}

.wood-menu-group {
  margin-bottom: 1rem;
  padding-inline: 1.5rem;
  position: relative;
}

.content-wrapper {
  position: relative;
  margin-top: auto;
  margin-bottom: auto;
}

h3 {
  padding-bottom: .4rem;
}


.nav-btn {
  position: absolute;
  z-index: 2;
  padding: .7rem .4rem .7rem .4rem;
  background-color: rgb(82, 50, 13);
  top: calc(50% - 1.4rem);
}

.nav-btn-left {
  left: -.5rem;
}

.nav-btn-right {
  right: -.5rem;
}

.slide-fade-enter-active, .slide-fade-leave-active {
  transition: .2s ease;
}

.slide-fade-leave-active {
  position: absolute;
  scale: 0.98;
}

.slide-fade-enter-from{
  opacity: 0;
  transform: scale(0);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: scale(0.98);
}

#koala-heatmap {
  float: left;
  margin-right: 1rem;
  max-width: min(40%, 10rem);
  position: relative;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'

const tutorialCount = 3;

export default defineComponent({
  name: "TutorialOverlay",
  data() {
    return {
      showIndex: 0
    }
  },

  methods: {
    loopIndex(index: number) {
      if (index < 0) {
        return tutorialCount - 1
      } else if (index >= tutorialCount) {
        return 0
      } else {
        return index
      }
    },
  }
})
</script>