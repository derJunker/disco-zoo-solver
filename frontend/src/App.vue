<template>
  <header class="rounded dock-top">
    <h1 @click="clickedLogo">Disco Zoo Solver</h1>
    <nav>
      <router-link class="btn btn-action-neutral-1" style="text-decoration: none; color: inherit;"
                   to="/reconstruct">Reconstruct
      </router-link>
      <router-link class="btn btn-action-neutral-2" style="text-decoration: none; color: inherit;" to="/accuracy">Accuracy</router-link>
      <router-link class="btn btn-action-neutral-2" style="text-decoration: none; color: inherit;" to="/battle">Battle</router-link>
      <router-link class="btn btn-action-neutral-3" style="text-decoration: none; color: inherit;" to="/stats">Stats
      </router-link>
      <router-link class="btn btn-action-info" style="text-decoration: none; color: inherit;" to="/about">About</router-link>
    </nav>
  </header>
  <router-view/>

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
header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: .5rem 1rem .7rem 1rem;
  background-color: var(--wood-color-dark);
  border: var(--border-large);
  box-shadow: var(--btn-shadow);
  border: var(--border-large) solid rgba(255, 255, 255, var(--border-light-opacity));
}

h1 {
  margin: 0;
  user-select: none;
  cursor: pointer;
}

nav {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  gap: 2rem;
  margin-right: 2rem;
}

</style>
