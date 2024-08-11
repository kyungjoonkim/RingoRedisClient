<template>
  <table>
    <thead>
    <tr>
      <th>Value</th>
    </tr>
    </thead>
    <tbody>
      <tr v-for="value in redisInfo.values">
        <td>{{ value }}</td>
      </tr>
    </tbody>
  </table>
</template>


<script setup>
import {defineProps,ref,watchEffect} from "vue"
const props = defineProps({data: Object})
const redisInfo = ref(props.data)
watchEffect(() => redisInfo.value = props.data)
</script>


<style scoped>

</style>