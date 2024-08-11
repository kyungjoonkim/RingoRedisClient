package kr.kkj.server.ringoredisclient.model.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParseResult {
    private ProtocolType protocolType;
    private Object result;
}
