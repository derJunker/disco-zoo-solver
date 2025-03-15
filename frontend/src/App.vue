<template>
  <div id="app-content">
    <router-view id="router-view"/>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    clickedLogo() {
      this.$router.push('/');
    }
  }
}
</script>

<style>
#app-content {
  height: 100%;
}
</style>
