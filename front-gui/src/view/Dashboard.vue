
<template>
  <div class="data-container">
    <div class="key-type-container">
      <div class="key">
        <h1> Key : {{targetKeyRedisData.key}}</h1>
      </div>
      <div class="type">
        <h1> Type : {{targetKeyRedisData.redisType}}</h1>
      </div>
    </div>

    <ZsetResultTable v-if="targetKeyRedisData.redisType === 'zset'" :data="targetKeyRedisData"/>
    <HashResultTable v-if="targetKeyRedisData.redisType === 'hash'" :data="targetKeyRedisData"/>
    <ListResultTable v-if="targetKeyRedisData.redisType === 'list'" :data="targetKeyRedisData"/>
    <SetResultTable v-if="targetKeyRedisData.redisType === 'set'" :data="targetKeyRedisData"/>
    <StringResultTable v-if="targetKeyRedisData.redisType === 'string'" :data="targetKeyRedisData"/>
  </div>
</template>

<script setup>
import {ref,watch} from "vue";
import {useRoute} from "vue-router";
import axios from "axios";
import { useSelectKeyInfoStore } from '@/stores/selectKeyStore.js'
import ZsetResultTable from "@/components/Reseults/ZsetResultTable.vue";
import HashResultTable from "@/components/Reseults/HashResultTable.vue";
import ListResultTable from "@/components/Reseults/ListResultTable.vue";
import SetResultTable from "@/components/Reseults/SetResultTable.vue";
import StringResultTable from "@/components/Reseults/StringResultTable.vue";

const route = useRoute()
const uid = route.query.uid
const selectKeyInfoStore = useSelectKeyInfoStore();
const targetKeyRedisData = ref({})

// selectKeyInfoStore.getKey가 변경될 때마다 getDataOf 함수를 실행
watch(() => selectKeyInfoStore.getKey, async (newVal, oldVal) => {
  console.log(`Key changed from ${oldVal} to ${newVal}`)
  targetKeyRedisData.value = await getDataOf(selectKeyInfoStore)
  console.log(targetKeyRedisData.value)

});


async function getDataOf(selectKeyInfo){
  console.log("Dashboard");
  try {
    const param = {
      params: {
        nodeId: selectKeyInfo.getNodeID,
        redisKey: selectKeyInfo.getKey
      },
    }
    const response = await axios.get(`/api/scan/data?id=${route.query.uid}`,param)
    return response.data
  }catch (err) {
    alert(err)
  }
}

</script>

<style>
.data-container{
  margin-left: 6vw;
}

.key-type-container {
  display: flex;
}

.key, .type {
  /* Apply any specific styling here */
  margin-right: 1vw; /* Example spacing, adjust as needed */
}

table {
  border-collapse: collapse;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  background-color: white;
}
th, td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}
th {
  background-color: #4CAF50;
  color: white;
}
tr:hover {
  background-color: #f5f5f5;
}
tr:nth-child(even) {
  background-color: #f9f9f9;
}
</style>