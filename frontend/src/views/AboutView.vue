<template>
  <div class="about">
    <div class="btn btn-gradient color-action-bad">
      Close
    </div>
  </div>
</template>
<style scoped>
* {
  margin: 1rem;
}
.btn {
  padding-top: 0.7rem;
  padding-bottom: 0.7rem;
  width: fit-content;
}

</style>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'AboutView',
});
</script>
