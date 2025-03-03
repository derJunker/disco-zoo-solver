<template>
  <div class="dropdown menu-item btn btn-action-neutral-2" @click="isOpen = !isOpen" :style="'min-width:'  +
  getLongestItem().length/1.2 + 'rem'">
    <span id="selectedItem" @click="isOpen = !isOpen">
      {{ itemMapper(selectedItem) }}
    </span>
    <svg viewBox="0 0 1030 638" width="10">
      <path d="M1017 68L541 626q-11 12-26 12t-26-12L13 68Q-3 49 6 24.5T39 0h952q24 0 33 24.5t-7 43.5z" fill="#FFF"></path>
    </svg>
    <div class="sub-menu" v-if="isOpen">
      <div v-for="(item, i) in items" :key="i" class="menu-item" @click="onClickSelect(item)">
        {{ itemMapper(item) }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'custom-dropdown',
  props: {
    defaultValue: {
      type: Object,
      required: false
    },
    items: {
      type: Array,
      required: true
    },

    itemMapper: {
      type: Function,
      required: false,
      default: (item) => item
    }
  },
  watch: {
    items: function (newItems) {
      if (!newItems.includes(this.selectedItem)) {
        if (newItems.length > 0)
          this.selectedItem = newItems[0]
        else
          this.selectedItem = null
      }
    }
  },
  methods: {
    onClickSelect (item) {
      this.selectedItem = item
    },

    getLongestItem () {
      return this.items.reduce((a, b) => this.itemMapper(a).length > this.itemMapper(b).length ? a : b)
    }
  },
  data () {
    return {
      selectedItem: this.defaultValue,
      isOpen: false
    }
  }
}
</script>

<style>
.menu-item {
  position: relative;
  min-width: fit-content;
  max-width: fit-content;
  margin-right: 10px;
}

.sub-menu {
  position: absolute;
  top: 100%;
  left: 0;
  border-radius: 5px;
  padding: 10px;
}

.menu-item * {
  user-select: none;
}

#selectedItem {
  cursor: pointer;
}

.dropdown {
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}
</style>