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
      <a id="something-1" class="btn btn-action-bad dock-bottom">Something</a>
      <a id="something-2" class="btn btn-action-neutral-2 dock-bottom">Something</a>
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

#app {
  min-height: 100vh;
}
header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin: 0 1rem 0 1rem;
  grid-row: 1;
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
  max-height: 10vh;
  z-index: 0;

  grid-row: 3;
}

#something-1 {
  left: calc(var(--border-small) * -1);
  border-top-left-radius: 0;
}
#something-2 {
  right: calc(var(--border-small) * -3);
  border-top-right-radius: 0;
}

#something-1, #something-2 {
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
  font-size: 2.25rem;
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
  display: grid;
  grid-template-rows: 3fr 30fr 1fr;
  gap: 0;
  height: 100vh;
}

</style>
