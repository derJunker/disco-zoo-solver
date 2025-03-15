<template>
  <span class="menu-bar">
    <span :class="firstColorClass + ' btn dock-bottom dock-left'" @click="onFirstButtonClick">{{ firstButtonName
                                                                                             }}</span>
    <span :class="secondColorClass + ' btn dock-bottom dock-right'" @click="onSecondButtonClick">{{ secondButtonName
                                                                                            }}</span>
  </span>
</template>

<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "MenuBar",
  props: {
    firstButtonName: {
      type: String,
      required: true
    },
    secondButtonName: {
      type: String,
      required: true
    },
    firstColorClass: {
      type: String,
      required: true
    },
    secondColorClass: {
      type: String,
      required: true
    },
    onFirstButtonClick: {
      type: Function,
      required: true
    },
    onSecondButtonClick: {
      type: Function,
      required: true
    }
  }
})
</script>

<style scoped>
.menu-bar {
  width: 100%;
  display: flex;
  justify-content: space-between;
  background-color: var(--wood-color-dark)
}

.menu-bar > span {
  padding: 1rem;
}
</style>