<template>
  <span class="menu-bar wood-menu dock-bottom dock-bottom-shadow dock-left dock-left">
    <button :class="firstColorClass + ' btn btn-gradient dock-bottom dock-left first'" @click="onFirstButtonClick">
      {{firstButtonName}}</button>
    <button :class="secondColorClass + ' btn btn-gradient dock-bottom dock-right second'" @click="onSecondButtonClick">
      {{secondButtonName}}
    </button>
  </span>
</template>

<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "MenuBar",
  props: {
    secondButtonName: {
      type: String,
      required: true
    },
    secondColorClass: {
      type: String,
      required: true
    },
    onSecondButtonClick: {
      type: Function,
      required: true
    },
    firstButtonName: {
      type: String,
      required: true
    },
    firstColorClass: {
      type: String,
      required: true
    },
    onFirstButtonClick: {
      type: Function,
      required: true
    },
  }
})
</script>

<style scoped>
.menu-bar {
  width: 100%;
  display: flex;
  justify-content: space-between;
  background-color: var(--wood-color-light);
  padding: 0;
  position: relative;
  height: 3rem;
  border-width: 4px;
  z-index: 50;
}

.first, .second {
  padding: 1rem;
  z-index: 1;
  bottom: -3px;
  position: fixed;
}

.first {
  left: -5px;
}

.second {
  right: -5px;
}
</style>