
export class KeyScanMenuInfo{
    constructor(response) {
        this.nodeID = response['nodeId']
        this.hostName = response['hostName']
        this.cursor = response['cursor']
        this.keys = response['keys'] || []
        this.keyJson = this.makeKeyMap() || {}
    }

    makeKeyMap(){
        if(!this.keys){
            return false
        }
        const keyMap = {}
        this.keys.forEach(key => {keyMap[key] = true})
        return keyMap
    }

    getKeys(){
        return this.keys || []
    }

    getNodeID(){
        return this.nodeID
    }

    getCursor() {
        return this.cursor
    }

    getKeyJson(){
        return this.keyJson
    }

    getHostName(){
        return this.hostName
    }

    /**
     * @param {KeyScanMenuInfo} currentKeyScan
     * */
    updateKeyInfo(currentKeyScan){
        if( !currentKeyScan || this.nodeID !== currentKeyScan.getNodeID() ){
            return false
        }

        if(currentKeyScan.getCursor() > 0){
            this.cursor = currentKeyScan.getCursor()
        }
        currentKeyScan.getKeys().forEach( key =>{
            if(this.keyJson[key]){
                return false
            }
            this.keys.push(key)
            this.keyJson[key] = true
        })
    }

    exist(key){
        const keyJson = this.keyJson;
        console.log(keyJson[key])
        return keyJson[key]
    }

    existNextKeyInfo(){
        return !!this.cursor
    }


    nextSearchKeyInfo(){
        return {
            'nodeId' : this.nodeID,
            'cursor' : this.cursor,
        }

    }






}