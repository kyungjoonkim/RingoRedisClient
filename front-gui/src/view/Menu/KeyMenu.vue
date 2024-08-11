
<template>
  <div class="menu-bar">
    <div>
      <BaseInput  style="border-color: azure" @keyup.enter="handleEnter" v-model="searchValue" />
    </div>
    <div ref="scrollContainer">
      <ul v-for="targetNodeInfo in keyScanInfos">
        <li>
          <a href="#" @click="actionFold(targetNodeInfo.getNodeID())">
            <span class="node-name"> {{targetNodeInfo.getHostName()}} </span>
          </a>

          <ul v-show="foldState[targetNodeInfo.getNodeID()]"
              @scroll.passive="handleScroll(targetNodeInfo)"
              class="scroll-container">

            <li style="margin-left: 1vw" v-for="key in targetNodeInfo.getKeys()">
              <a href="#" @click="getTargetKeyInfo(key)">{{key}}</a>
            </li>

          </ul>
        </li>

      </ul>
    </div>

  </div>
</template>

<script setup>
  import BaseInput from "@/components/Inputs/BaseInput.vue"
  import {ref,reactive} from "vue"
  import {useRoute} from "vue-router"
  import axios from "axios"
  import {KeyScanMenuInfo} from '@/domain/KeyScanMenuInfo.js'
  import { useSelectKeyInfoStore } from '@/stores/selectKeyStore.js'


  const route = useRoute()
  const keyScanInfos = reactive([])
  const scrollContainer = ref(null)
  const foldState = reactive({})
  const searchValue = ref()

  getRedisKeyItems(keyScanInfos,true)

  /**
   * 전역변수를 파라미터로 넣은 이유는 콘솔에서 디버그가 힘들어서... 지역변수로 받아서 처리하였음
   * @param {KeyScanMenuInfo[]} globalKeyInfos
   * @param {boolean} init
   * */
  async function getRedisKeyItems(globalKeyInfos,init){
    const nextKeyInfos = globalKeyInfos.filter(keyInfo => keyInfo.existNextKeyInfo())
                                 .map(keyInfo => keyInfo.nextSearchKeyInfo())

    const currentKeyInfos = await getKeysInfos(nextKeyInfos,init)
    if( !currentKeyInfos ){
      return false
    }

    await updateGlobalScanInfos(globalKeyInfos,currentKeyInfos);
  }

  const handleScroll = (targetNodeInfo) => {
    const container = scrollContainer.value
    if (container.scrollTop + container.clientHeight >= container.scrollHeight - 5) { // 스크롤이 거의 끝에 도달했는지 확인
      getRedisKeyItems([targetNodeInfo],false)
    }
  };



  /**
   * @param {KeyScanMenuInfo[]} globalKeyInfos
   * @param {KeyScanMenuInfo[]} currentKeyInfos
   * */
  async function updateGlobalScanInfos(globalKeyInfos, currentKeyInfos) {
    if( !globalKeyInfos.length ){
      currentKeyInfos.forEach(info =>{
        globalKeyInfos.push(info)
      })
      return true
    }

    const globalKeyNodeJson = globalKeyInfos.reduce((result, item) => {
      result[item.getNodeID()] = item
      return result
    }, {})

    for( const curInfo of currentKeyInfos){
      if( !globalKeyNodeJson[curInfo.getNodeID()] ){
        globalKeyInfos.push(curInfo)
      }

      globalKeyNodeJson[curInfo.getNodeID()]
          .updateKeyInfo(curInfo)
    }

    return true
  }


  async function getKeysInfos(nextScanInfo,init) {
    try {
      const response = await axios.post(`/api/scan/list?id=${route.query.uid}`, {
        "count" : 20,
        "scanNodeInfos" : nextScanInfo || [],
        "init" : init
      })

      if( !response.data ){
        return []
      }

      return response.data.map(info => new KeyScanMenuInfo(info));
    } catch (err) {
      alert(err)
    }
  }

  const selectKeyInfoStore = useSelectKeyInfoStore();
  function getTargetKeyInfo(key){

    let keyScanInfo;
    for(const info of keyScanInfos){
      const keyJson = info.getKeyJson();
      if (keyJson[key]){
        keyScanInfo = info
      }
    }

    if( !keyScanInfo ){
      alert("No Found Key Info")
      return false
    }

    selectKeyInfoStore.setSelectedKey(keyScanInfo.getNodeID(),key)
  }

  function actionFold(nodeId){
    foldState[nodeId] = !foldState[nodeId]
  }

  async function handleEnter(event) {
    event.stopPropagation()

    try{
      const response = await axios.get(`/api/scan/search?id=${route.query.uid}`,
          {
            params: {
              "count": 20,
              "searchValue" : searchValue.value
            }
          }
      )
      console.log(response.data)
    }catch (err) {
      alert(err)
    }
  }
</script>

<style scoped>
  .menu-bar {
    position: fixed;
    top: 0;
    left: 0;
    width: 200px;
    height: 100%;
    background-color: #333;
    color: white;
    padding: 10px;
    overflow-y: auto;
  }

  .scroll-container {
    overflow-y: auto;
    height: 30vw;
  }

  .menu-bar ul {
    list-style-type: none;
    padding: 0;
  }

  .menu-bar li {
    margin: 10px 0;
  }

  .menu-bar a {
    color: white;
    text-decoration: none;
  }

  .node-name {
    color: white !important;
  }
</style>