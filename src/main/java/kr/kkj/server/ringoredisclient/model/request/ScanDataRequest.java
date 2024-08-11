package kr.kkj.server.ringoredisclient.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScanDataRequest {
    private String nodeId;
    private String redisKey;
}
