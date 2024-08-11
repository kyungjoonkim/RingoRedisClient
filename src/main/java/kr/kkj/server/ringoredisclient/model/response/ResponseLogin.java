package kr.kkj.server.ringoredisclient.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseLogin {
    private final String uuid;
    private final boolean success;
}
