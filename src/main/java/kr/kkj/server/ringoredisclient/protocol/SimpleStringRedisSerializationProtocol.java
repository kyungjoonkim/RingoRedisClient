package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Getter;

import java.io.InputStream;

@Getter
public class SimpleStringRedisSerializationProtocol implements RedisSerializationProtocol {

    private String serialize;

    public ProtocolType type(){
        return ProtocolType.SIMPLE_STRING;
    }

    @Override
    public String serialize(String[] command){
        return this.serialize = type().getType() + command[0] + END;
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        return this.readLine(inputStream);
    }


}
