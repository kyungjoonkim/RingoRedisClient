package kr.kkj.server.ringoredisclient.model.request;

import lombok.Getter;

@Getter
public class ScanRequestData {
    private String nodeId;
    private String redisKey;

}
