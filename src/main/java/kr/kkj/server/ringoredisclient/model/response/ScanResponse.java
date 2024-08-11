package kr.kkj.server.ringoredisclient.model.response;

import kr.kkj.server.ringoredisclient.connection.info.ClusterRedisConnectionInfo;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScanResponse {

    private final String nodeId;
    private final String hostName;
    private int cursor;
    private final List<String> keys;

    public ScanResponse(ClusterRedisConnectionInfo.HostInfo hostInfo, Object scabResponse) {
        this.nodeId = hostInfo.getNodeId();
        this.hostName = hostInfo.getIp();
        this.keys = new ArrayList<>();
        if(scabResponse == null){
            return;
        }

        List<Object> scanInfo = (List<Object>) scabResponse;
        this.cursor = Integer.parseInt((String)scanInfo.getFirst());
        if(this.emptyKeys(scanInfo)){
            return;
        }

        var resKeys = (List<String>) scanInfo.getLast();
        if(CollectionUtils.isEmpty(resKeys)){
            return;
        }
        this.keys.addAll(resKeys);
    }

    private boolean emptyKeys(List<Object> scanInfo){
        return scanInfo.getFirst() == scanInfo.getLast();
    }

    public String getLastKey(){
        if(CollectionUtils.isEmpty(this.getKeys())){
            return "";
        }
        return this.keys.getLast();
    }




}
