package kr.kkj.server.ringoredisclient.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
public class RetrievedKeyResponse {
    private int cursor;
    private String key;
    private String redisType;
    private boolean fair;
    private List<String> values;
    private Map<String,String> fairValues;

}
