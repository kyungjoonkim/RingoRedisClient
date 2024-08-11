package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Getter
public class ErrorRedisSerializationProtocol implements RedisSerializationProtocol{

    private String serialize;

    public ProtocolType type() {
        return ProtocolType.ERROR;
    }

    public String serialize(String[] command) {
        return this.serialize = type().getType() + command[0] + RedisSerializationProtocol.END;
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        try {
            throw new RuntimeException(new BufferedReader(new InputStreamReader(inputStream)).readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
