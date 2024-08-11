<template>
  <div class="row">
    <div class="col-lg-9 login-box-center">

      <card>
        <div class="login-header-title">
          <h2 slot="header" class="title">Redis Connection</h2>
        </div>

        <div class="col-md-3 px-md-1">
          <BaseInput label="Host" type="text" placeholder="127.0.0.1" v-model="model.host"/>
        </div>

        <div class="col-md-3 px-md-1">
          <BaseInput label="Port" type="number" placeholder="6379" v-model="model.port"/>
        </div>

        <div class="col-md-3 px-md-1">
          <BaseInput label="Username" type="string" v-model="model.username"/>
        </div>

        <div class="col-md-3 px-md-1">
          <BaseInput label="Possword" type="password" v-model="model.password"/>
        </div>

        <div class="login-header-title">
          <BaseButton slot="footer" type="primary" fill @click="connectServer">Connect</BaseButton>
        </div>

      </card>

    </div>

  </div>
</template>
<script setup>
import { ref } from 'vue';
import BaseInput from "../components/Inputs/BaseInput.vue";
import Card from "../components/Cards/Card.vue";
import BaseButton from "@/components/BaseButton.vue";
import {useConnectStore} from '../stores/connectStore.js';
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios';

const isLocal = (import.meta.env.VITE_EVN === 'local')

const model = ref({
  host: isLocal ? '172.23.0.2' : 'localhost',
  port: 6379,
  username: '',
  password: isLocal ? 'bitnami' : ''
});

console.log(import.meta.env.VITE_EVN)

const userStore = useConnectStore()
const router = useRouter()

const connectServer = async () => {
  try{
    userStore.setInputInfo(model.value.host,model.value.port, model.value.username, model.value.password);
    const response = await axios.post('/api/login', {
      host: model.value.host,
      port: model.value.port,
      userName: model.value.username,
      password: model.value.password
    })
    userStore.setUuid(response.data.uuid);

    await router.push(`/dashboard/?uid=${response.data.uuid}`);
  }catch (error){
    alert(error)
  }

}
</script>

<style scoped>
.login-box-center {
  border: 2px solid red;
  margin: 15vw 15vw 15vw 0vw;

}

.login-header-title {
  text-align: center;
}


</style>