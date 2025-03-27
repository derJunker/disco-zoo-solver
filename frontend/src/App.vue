<template>
  <div id="app-content">
    <error-handler/>
    <router-view id="router-view"/>
  </div>
</template>

<script>
import ErrorHandler from "@/components/Basic/ErrorHandler.vue";

export default {
  name: 'App',
  components: {ErrorHandler},
  methods: {
  }
}
</script>

<style>
#app-content {
  height: 100%;
  position: relative;
}
#error-handler {
  position: absolute;
  left: 0;
  right: 0;
  margin: auto;
  z-index: 1000;
}
</style>
