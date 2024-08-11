// store/dashboardStore.js
import { defineStore } from 'pinia';

export const useSelectKeyInfoStore = defineStore('select-key-info', {
    state: () => ({
        nodeID: null,
        selectedKey: null,
    }),
    actions: {
        setSelectedKey(nodeID,key) {
            this.nodeID = nodeID;
            this.selectedKey = key;
        },
    },
    getters :{
        getKey(){
            return this.selectedKey;
        },
        getNodeID(){
            return this.nodeID;
        },

    }
});