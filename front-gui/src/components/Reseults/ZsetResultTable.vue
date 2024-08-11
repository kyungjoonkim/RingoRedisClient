<template>
  <table>
    <thead>
    <tr>
      <th>Value</th>
      <th>Score</th>
    </tr>
    </thead>
    <tbody>

    <tr v-for="(score,zValue) in redisInfo.fairValues">
      <td>{{ zValue }}</td>
      <td>{{ score }}</td>
    </tr>

    </tbody>
  </table>
</template>


<script setup>
import {defineProps, ref, watchEffect} from "vue"
const props = defineProps({data: Object})
const redisInfo = ref(props.data)
watchEffect(() => redisInfo.value = props.data)
</script>


<style scoped>

</style>