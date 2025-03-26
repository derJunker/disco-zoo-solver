<template>
  <button class="toggle-select btn btn-gradient" @click="onClick" :class = "selected ? selectedColorClass :
  'color-action-disabled'">
    {{ title }}
  </button>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'toggle-select',
  props: {
    title: {
      type: String,
      required: true
    },
    defaultValue: {
      type: Boolean,
      required: false
    },
    selectedColorClass: {
      type: String,
      required: true
    },
  },

  data() {
    return {
      selected: this.defaultValue
    }
  },

  created() {
    this.selected = this.defaultValue
    this.$emit('selected', this.selected)
  },

  methods: {
    onClick() {
      this.selected = !this.selected
      this.$emit('selected', this.selected)
    }
  },

  watch: {
    defaultValue(newVal) {
      this.selected = newVal
      this.$emit('selected', this.selected)
    }
  }
})
</script>

<style scoped>
</style>