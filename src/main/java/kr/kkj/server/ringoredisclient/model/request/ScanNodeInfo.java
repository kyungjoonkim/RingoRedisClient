package kr.kkj.server.ringoredisclient.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Builder
@AllArgsConstructor
public class ScanNodeInfo {

    private int cursor;
    private int count;
    private String nodeId;
    private String redisKey;

    public ScanNodeInfo() {}

    public int getCount() {
        if(this.count <= 0){
            this.count = 100;
        }
        if(this.count > 500){
            this.count = 100;
        }

        return this.count;
    }


    public String getStrCursor(){
        return String.valueOf(this.getCursor());
    }

    public String getStrCount(){
        return String.valueOf(this.getCount());
    }

    public boolean isEmpty() {
        return !StringUtils.hasText(this.nodeId);
    }

}
