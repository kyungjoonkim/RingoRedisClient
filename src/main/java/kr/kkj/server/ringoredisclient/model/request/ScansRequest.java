package kr.kkj.server.ringoredisclient.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScansRequest {
    private int count;
    private List<ScanNodeInfo> scanNodeInfos;
    private boolean init;

    public int getCount() {
        if(this.count <= 0){
            this.count = 100;
        }
        if(this.count > 500){
            this.count = 100;
        }

        return this.count;
    }

    public List<ScanNodeInfo> getScanNodeInfos() {
        if( this.scanNodeInfos == null ){
            this.scanNodeInfos = new ArrayList<>();
        }
        return scanNodeInfos;
    }

    public Map<String,ScanNodeInfo> getScanNodeInfoMap() {
        var infos = this.getScanNodeInfos();
        return infos.stream().collect(Collectors.toMap(ScanNodeInfo::getNodeId, Function.identity()));
    }

    public String getStrCount() {
        return String.valueOf(this.getCount());
    }

    public boolean isNext(){
        return !this.init;
    }
}
