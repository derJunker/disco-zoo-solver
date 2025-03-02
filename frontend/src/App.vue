<template>
  <header class="rounded dock-top">
    <h1 @click="clickedLogo">Disco Zoo Solver</h1>
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
  <router-view/>
  <footer class="dock-bottom">
    <a id="something-1" class="btn btn-action-bad dock-bottom">Something</a>
    <a id="something-2" class="btn btn-action-neutral-2 dock-bottom">Something</a>
    <a :href="$baseUrl + '/pages/impressum.html'">Impressum / Contact</a>
    <div>
      <a :href="$baseUrl + '/pages/privacy-policy-de.html'">Datenschutzerklärung</a>
      /
      <a :href="$baseUrl + '/pages/privacy-policy-en.html'">Privacy Policy</a>
    </div>
  </footer>

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

footer {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 1.5rem;

  padding: .5rem 1rem .7rem 1rem;
  background-color: var(--wood-color-light);
  box-shadow: var(--btn-shadow);
  border: var(--border-large) solid rgba(255, 255, 255, var(--border-light-opacity));
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

#something-1 {
  position: fixed;
  left: calc(var(--border-small) * -1);
  border-top-left-radius: 0;
}
#something-2 {
  position: fixed;
  right: calc(var(--border-small) * -1);
  border-top-right-radius: 0;
}

#something-1, #something-2 {
  padding-bottom: 1.25rem;
  padding-top: 1.25rem;

  bottom: calc(var(--border-small) * -1);

  cursor: pointer;
  user-select: none;
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
