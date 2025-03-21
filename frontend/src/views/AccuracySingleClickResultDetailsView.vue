<template>
  <div class="accuracy-single-click-result-details-view">
    <div class="accuracy-single-click-result-details-content">
      <div class="wood-menu menu-bottom dock-bottom" id="details-menu">
        <h1>Accuracy Single Click Result Details</h1>
        <div class="stats wood-menu-group">
          <h2>Game {{showIndex+1}}</h2>
          <div class="animals">
            <animal-square :animal="animal" v-for="animal in getContainedAnimals(showIndex)"
                           :key="animal.name"
                           class="animal-square" :class="singleClickHistory[showIndex].animalToFind.name ===
                           animal.name ? 'animal-highlighted' : ''"/>
          </div>
          <div class="boardNav">
            <div @click="showIndex = loopIndex(showIndex-1)">{{"<"}}</div>
            <div class="tinyBoard rounded"></div>
            <div @click="showIndex = loopIndex(showIndex+1)">{{">"}}</div>
          </div>
          <div class="score">Score: {{(singleClickHistory[showIndex].score*100).toFixed(2)}}%</div>
        </div>
      </div>
    </div>
    <menu-bar :on-first-button-click="console.log" first-color-class="color-action-neutral-1" first-button-name="sth"
              :on-second-button-click="console.log" second-color-class="color-action-neutral-2"
              second-button-name="sth"/>
  </div>
</template>

<style scoped>
.accuracy-single-click-result-details-view {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.accuracy-single-click-result-details-content {
  flex: 1;
  position: relative;
}
#details-menu {
  max-width: min(90%, 500px);
}
.stats {
  display: flex;
  flex-direction: column;
}

.animals {
  display: flex;
  flex-direction: row;
  gap: .3rem;
  justify-content: center;
  align-items: center;
}

.animal-square {
  max-height: 3.5rem;
}

.boardNav {
  flex: 1;
  aspect-ratio: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: .3rem;
}

.tinyBoard {
  flex: 1;
  aspect-ratio: 1;
  background-color: var(--wood-color-dark);
  margin: 3rem;
}

h2 {
  margin-bottom: .8rem;
}

h2, .score {
  text-align: center;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'
import MenuBar from "@/components/MenuBar.vue";
import {useAccuracyState} from "@/store/useState";
import AnimalSquare from "@/components/Basic/AnimalSquare.vue";

const state = useAccuracyState()

export default defineComponent({
  name: "AccuracySingleClickResultDetailsView",
  components: {AnimalSquare, MenuBar},
  data() {
    return {
      singleClickHistory: state.singleClickHistory,
      showIndex: 0
    }
  },

  methods: {
    getContainedAnimals(index: number) {
      return this.singleClickHistory[index].game.containedAnimals
    },

    loopIndex(index: number) {
      if (index < 0) {
        return this.singleClickHistory.length - 1
      } else if (index >= this.singleClickHistory.length) {
        return 0
      } else {
        return index
      }
    }
  }
})
</script>