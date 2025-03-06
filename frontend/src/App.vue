<template>
  <div id="app-content">
    <header class="rounded config-board dock-top btn-shadow">
      <h1 id="logo" @click="clickedLogo">Disco Zoo Solver</h1>
      <nav>
        <router-link class="btn btn-header btn-action-neutral-1" style="text-decoration: none; color: inherit;"
                     to="/reconstruct">Reconstruct
        </router-link>
        <router-link class="btn btn-header btn-action-neutral-2" style="text-decoration: none; color: inherit;"
                     to="/accuracy">
          Accuracy</router-link>
        <router-link class="btn btn-header btn-action-neutral-2" style="text-decoration: none; color: inherit;"
                     to="/battle">
          Battle</router-link>
        <router-link class="btn btn-header btn-action-neutral-3" style="text-decoration: none; color: inherit;"
                     to="/stats">Stats
        </router-link>
        <router-link class="btn btn-header btn-action-info" style="text-decoration: none; color: inherit;"
                     to="/about">About
        </router-link>
      </nav>
    </header>
    <router-view id="router-view"/>
    <footer class="dock-bottom">
      <a :href="$baseUrl + '/pages/impressum.html'">Impressum / Contact</a>
      <div>
        <a :href="$baseUrl + '/pages/privacy-policy-de.html'">Datenschutzerklärung</a>
        /
        <a :href="$baseUrl + '/pages/privacy-policy-en.html'">Privacy Policy</a>
      </div>
      <a id="util-btn-1" class="btn btn-action-bad dock-bottom">Nav</a>
      <a id="util-btn-2" class="btn btn-action-neutral-2 dock-bottom">Config</a>
    </footer>
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
header {
  position: sticky;
  top: 0;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  overflow: hidden;

  border-left: none!important;
  border-right: none!important;
}

@media (min-width: 900px) {
  header {
    margin: 0 1rem;
    border: var(--border-large)!important;
    border: var(--border-large) solid rgba(255, 255, 255, var(--border-light-opacity))!important;
  }

  #util-btn-1, #util-btn-2 {
    display: none;
  }
}

footer {
  position: sticky;
  bottom: 0;
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 1.5rem;

  padding: .5rem 1rem .7rem 1rem;
  background-color: var(--wood-color-light);
  box-shadow: var(--btn-shadow);
  border: var(--border-large) solid rgba(255, 255, 255, var(--border-light-opacity));
  border-left: none;
  border-right: none;
  border-top-left-radius: 0!important;
  border-top-right-radius: 0!important;
  max-height: 10vh;
  z-index: 0;
  box-sizing: border-box;
}

#util-btn-1 {
  left: calc(var(--border-small) * -1);
  border-top-left-radius: 0;
}
#util-btn-2 {
  right: calc(var(--border-small) * -3);
  border-top-right-radius: 0;
}

#util-btn-1, #util-btn-2 {
  position: fixed;

  padding-bottom: 1.25rem;
  padding-top: 1.25rem;

  bottom: calc(var(--border-small) * -1);

  cursor: pointer;
  user-select: none;
  z-index: 12;
}

#logo {
  margin: 0;
  user-select: none;
  cursor: pointer;
  font-size: min(2.25rem, 3vw);
  text-wrap: nowrap;
}

nav {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  gap: 2rem;
  margin-right: 2rem;
}

#app-content {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 0;
  height: 100vh;
  width: 100vw;
}

#router-view {
  flex: 1;
  overflow-y: auto;
}

</style>
