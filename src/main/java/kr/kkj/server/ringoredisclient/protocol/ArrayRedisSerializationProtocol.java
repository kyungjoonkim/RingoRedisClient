package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.RedisProtocolParser;
import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ArrayRedisSerializationProtocol implements RedisSerializationProtocol {

    private String serialize;

    public ProtocolType type(){
        return ProtocolType.ARRAY;
    }

    @Override
    public String serialize(String[] command){
        var sb = new StringBuilder();
        sb.append(type().getType())
          .append(command.length)
          .append(RedisSerializationProtocol.END);

        for(String target : command){
            sb.append(target)
              .append(RedisSerializationProtocol.END);
        }

        return serialize = sb.toString();
    }

    public String serialize(RedisSerializationProtocol... protocols){
        StringBuilder sb = new StringBuilder();
        sb.append(type().getType())
                .append(protocols.length)
                .append(RedisSerializationProtocol.END);

        for(var protocol : protocols){
            sb.append(protocol.getSerialize());
        }

        return sb.toString();
    }

    public <T extends RedisSerializationProtocol> String serialize(List<T> protocols){
        if(CollectionUtils.isEmpty(protocols)){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(type().getType())
                .append(protocols.size())
                .append(RedisSerializationProtocol.END);

        for(var protocol : protocols){
            sb.append(protocol.getSerialize());
        }

        return sb.toString();
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        List<Object> result = new ArrayList<>();
        int length;
        try {
            length = Integer.parseInt(this.readLine(inputStream));
            if (length == -1) {
                return null; // Null array
            }

            for (int i = 0; i < length; i++) {
                result.add(RedisProtocolParser.readResponseWithParse(inputStream));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }



}
