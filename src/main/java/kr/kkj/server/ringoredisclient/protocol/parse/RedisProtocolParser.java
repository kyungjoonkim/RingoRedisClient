package kr.kkj.server.ringoredisclient.protocol.parse;

import kr.kkj.server.ringoredisclient.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class RedisProtocolParser {

    public static Map<ProtocolType, RedisSerializationProtocol> protocolMap;
    static {
        protocolMap = Map.of(
                ProtocolType.BULK_STRING, new BulkStringRedisSerializationProtocol(),
                ProtocolType.ARRAY, new ArrayRedisSerializationProtocol(),
                ProtocolType.ERROR, new ErrorRedisSerializationProtocol(),
                ProtocolType.INTEGER, new IntegerRedisSerializationProtocol(),
                ProtocolType.SIMPLE_STRING, new SimpleStringRedisSerializationProtocol()
        );

    }

    public static Object readResponseWithParse(InputStream inputStream) throws IOException {
        var type = String.valueOf( (char) inputStream.read());
        var protocol = protocolMap.get(ProtocolType.findType(type));
        return protocol.deserialize(inputStream);
    }

    public static void printOf(Object result) {
        if (result instanceof String) {
            System.out.println(result);
        } else if (result instanceof Long) {
            System.out.println(result);
        } else if (result instanceof byte[]) {
            System.out.println(new String((byte[]) result));
        } else if (result instanceof List) {
            for (Object o : (List) result) {
                 printOf(o);
            }
        }

    }


}
