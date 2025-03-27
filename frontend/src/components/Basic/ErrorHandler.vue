<template>
  <div id="error-handler">
    <transition-group>
      <div v-for="error in errors" :key="error" class="error rounded" @click="onClick(error)">
        <div>{{error}}</div>
      </div>
    </transition-group>
  </div>
</template>

<style scoped>
#error-handler {
  display: flex;
  flex-direction: column;
  max-width: 25rem;
  gap: .3rem;
  margin-top: .5rem;
}
.error {
  margin: auto;
  font-size: .8rem;
  background-color: red;
  border-radius: var(--border-radius);
  max-width: fit-content;
  padding: 1rem;
}

.v-enter-active, .v-leave-active {
  transition: opacity 0.5s ease, transform 0.5s ease;
}

.v-enter-from {
  opacity: 0;
  transform: translateY(-20px);
}

.v-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

</style>

<script lang="ts">
import {defineComponent} from 'vue'
import {useErrors} from "@/store/useErrors";

export default defineComponent({
  name: "ErrorHandler",
  data() {
    return {
      errorState: useErrors(),
      errors: useErrors().errors
    }
  },
  methods: {
    onClick(error: string) {
      this.errorState.removeError(error)
    }
  }
})
</script>