<template>
  <span :class="'dropdown btn btn-action-neutral-2 ' + (isOpen? 'dock-bottom' : '')" @click="isOpen = !isOpen"
       :style="'min-width:'  +
  getLongestItemSize()/1.2 + 'rem'"
        @focusout="handleFocusOut"
        tabindex="0">
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
  name: 'dropdown-select',
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

  created() {
    if(!this.defaultValue && this.items.length > 0) {
      this.selectedItem = this.items[0]
    } else {
      this.selectedItem = this.defaultValue
    }

    this.$emit('item-selected', this.selectedItem);
  },

  methods: {
    onClickSelect (item) {
      if(this.itemMapper(item) === this.itemMapper(this.selectedItem)) {
        return
      }
      this.selectedItem = item
      this.$emit('item-selected', item);
    },

    getLongestItemSize () {
      let returnVal = 0;
      if (!this.items || this.items.length === 0)
        returnVal = 0
      else
        returnVal = this.items.map(this.itemMapper).reduce((a, b) => a.length >
        b.length ?
          a :
          b).length
      return returnVal
    },

    handleFocusOut() {
      this.isOpen = false
    }
  },
  data () {
    return {
      selectedItem: null,
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
  z-index: 100;
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  gap: .25rem
}

.menu-item * {
  user-select: none;
}

#selectedItem {
  cursor: pointer;
}

/* actually used */
.dropdown {
  position: relative;
  display: flex;
  align-items: stretch;
  gap: .25rem;
  justify-content: space-between;
  cursor: pointer;
  padding: .5rem;
  user-select: none;
}
</style>