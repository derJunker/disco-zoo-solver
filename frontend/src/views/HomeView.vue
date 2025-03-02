<template>
  <div class="home">
    {{ msg }}
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import HelloWorld from '@/components/HelloWorld.vue';
import {useAnimals} from "@/store/useAnimals"; // @ is an alias to /src

export default defineComponent({
  name: 'HomeView',
  data() {
    return {
      msg : "" as string,
    }
  },

  async created() {
    var animals = useAnimals();
    this.msg = await animals.getAnimals();
  },
});
</script>
