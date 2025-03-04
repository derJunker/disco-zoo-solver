<template>
  <span :class="'dropdown btn btn-action-neutral-2 ' + (isOpen? 'dock-bottom' : '')" @click="isOpen = !isOpen"
       :style="'min-width:'  +
  getLongestItemSize()/1.2 + 'rem'">
    <span id="selectedItem">
      {{ itemMapper(selectedItem) }}
    </span>
    <svg viewBox="0 0 1030 638" width="10">
      <path d="M1017 68L541 626q-11 12-26 12t-26-12L13 68Q-3 49 6 24.5T39 0h952q24 0 33 24.5t-7 43.5z" fill="#FFF"></path>
    </svg>
    <div class="sub-menu btn-action-neutral-2 rounded dock-top" v-if="isOpen" :style="'min-width:' +
    getLongestItemSize()/1.2 + 'rem'">
      <div v-for="(item, i) in items" :key="i" class="menu-item" @click="onClickSelect(item)">
        {{ itemMapper(item) }}
      </div>
    </div>
  </span>
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
          if (this.selectedItem !== newItems[0])
            this.onClickSelect(newItems[0])
        else {
            this.selectedItem = null
            this.onClickSelect(null)
        }
      }
    }
  },
  methods: {
    onClickSelect (item) {
      this.selectedItem = item
      this.$emit('item-selected', item);
    },

    getLongestItemSize () {
      if (!this.items || this.items.length === 0)
        return 0
      return this.items.reduce((a, b) => this.itemMapper(a).length > this.itemMapper(b).length ? a : b).length
    },
  },
  data () {
    return {
      selectedItem: this.defaultValue,
      isOpen: false
    }
  },

}
</script>

<style>
.menu-item {
  text-align: left;
  padding-bottom: .2rem;
}

.sub-menu {
  position: absolute;
  top: 100%;
  left: 0;
  padding: .5rem;
  box-shadow: var(--btn-shadow-and-border);
}

.menu-item * {
  user-select: none;
}

#selectedItem {
  cursor: pointer;
}

/* actually used */
.dropdown {
  max-width: fit-content;
  position: relative;
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  cursor: pointer;
  padding: .5rem;
  user-select: none;
}
</style>