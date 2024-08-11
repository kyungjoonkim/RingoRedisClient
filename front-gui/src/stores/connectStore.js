import {defineStore}  from "pinia";

export const useConnectStore = defineStore({
    id: 'connection',
    state: () => ({
        uuid: "",
        inputInfo :{
            host: "",
            port: 6379,
            username: "",
            password: "",
        }
    }),
    actions: {
        setInputInfo(host, port, username, password){
            this.inputInfo.host = host;
            this.inputInfo.port = port;
            this.inputInfo.username = username;
            this.inputInfo.password = password;
        },
        setUuid(uuid){
            this.uuid = uuid;
        },
    },
    getters:{
        getHost(){
            return this.inputInfo.host;
        },

        getPort(){
            return this.inputInfo.port;
        },

        getUsername(){
            return this.inputInfo.username;
        },

        getPassword(){
            return this.inputInfo.password;
        },
        getUUID(){
            return this.uuid;
        },
        isLogin(){
            return !!this.getUUID
        },
        isNotLogin(){
            return !this.isLogin
        }


    }
});
