package kr.kkj.server.ringoredisclient;

import kr.kkj.server.ringoredisclient.connection.info.RedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedisUserContext {
    private final String userUUID;
    private final ConnectRequest inputRequestInfo;
    private final RedisConnectionInfo connectionInfo;


}
