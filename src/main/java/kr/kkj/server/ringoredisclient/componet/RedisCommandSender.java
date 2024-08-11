package kr.kkj.server.ringoredisclient.componet;

import kr.kkj.server.ringoredisclient.protocol.ArrayRedisSerializationProtocol;
import kr.kkj.server.ringoredisclient.protocol.BulkStringRedisSerializationProtocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisCommandSender {

    public static void sendNativeCommand(OutputStream outputStream, String[] strCommands){
        var bulkProtocols = Arrays.stream(strCommands)
                .map(BulkStringRedisSerializationProtocol::new)
                .toList();

        var nativeCommand = new ArrayRedisSerializationProtocol().serialize(bulkProtocols);
        try {
            outputStream.write(nativeCommand.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO 참고 https://claude.ai/chat/5d3f519e-3c05-49cc-b82f-582e8c2b6a35
    public static void sendNativeMultiCommand(OutputStream outputStream, List<String[]> groupCommands){
        List<BulkStringRedisSerializationProtocol> result = new ArrayList<>();
        result.add(new BulkStringRedisSerializationProtocol("MULTI"));

        for( var commands : groupCommands ){
            Arrays.stream(commands)
                    .map(BulkStringRedisSerializationProtocol::new)
                    .toList();
        }

        result.add(new BulkStringRedisSerializationProtocol("EXEC"));
        var nativeCommand = new ArrayRedisSerializationProtocol().serialize(result);
        try {
            outputStream.write(nativeCommand.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
