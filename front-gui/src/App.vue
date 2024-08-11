<template>
  <main>

    <!-- 로그인 상태에 따라 조건부로 KeyMenu를 렌더링 -->
    <KeyMenu v-if="existUid" />
    <div class="content-body">
      <RouterView v-slot="{ Component }">
        <div style="padding: 0 33vw;">
          <h1>Hello App!</h1>
          <p>
            <strong>현재 라우트 경로:</strong> {{ $route.fullPath }}
          </p>
        </div>
        <div>
          <component :is="Component" />
        </div>
      </RouterView>
    </div>





  </main>
</template>

<script setup>
import {useConnectStore} from '@/stores/connectStore.js';
import KeyMenu from "@/view/Menu/KeyMenu.vue";
import {useRoute} from "vue-router";
import { computed } from 'vue'

  const route = useRoute()
  const existUid = computed(() => !!route.query.uid)
  console.log(`existUid: ${existUid}`)

  // const store = useConnectStore();

</script>



<style scoped>
  main{
    width: 100%;
  }
  .content-body{
    position: relative;
    left: 200px;
  }
</style>
