package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Getter;

import java.io.InputStream;

@Getter
public class IntegerRedisSerializationProtocol implements RedisSerializationProtocol {
    private String serialize;

    public ProtocolType type() {
        return ProtocolType.INTEGER;
    }

    public String serialize(String[] command) {
        return this.serialize = type().getType()+ command[0] + END;
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        var strNumber = this.readLine(inputStream);
        return Long.parseLong(strNumber);
    }


}