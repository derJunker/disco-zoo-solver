<template>
 <span class="labeled-checkbox">
   <button :id="inputId" :class="{ 'checked': checked }" @click="$emit('checked', !checked)"></button>
   <label :for="inputId" @click="$emit('checked', !checked)">{{ label }}</label>
 </span>
</template>

<style>
.labeled-checkbox {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.labeled-checkbox button {
  cursor: pointer;
  width: 1rem;
  height: 1rem;
  background-color: black;
  border: white solid 2px;
  border-radius: 3px;
  transition: background-color 0.1s ease-in-out, border-color 0.1s ease-in-out, transform 0.05s ease-in-out;
}

.labeled-checkbox button:active {
  transform: scale(1.05);
}

.checked {
  background-color: white !important;
  border-color: black !important;
}
</style>

<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "LabledCheckBox",
  emits: ['checked'],
  props: {
    inputId: {
      type: String,
      required: true
    },
    label: {
      type: String,
      required: true
    },
    checked: {
      type: Boolean,
      default: false
    }
  },
  methods: {

  }
})
</script>