package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;
import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Getter
public class BulkStringRedisSerializationProtocol implements RedisSerializationProtocol {

    private static final int BUFFER_SIZE = 1024;

    private String serialize;

    public ProtocolType type() {
        return ProtocolType.BULK_STRING;
    }
    public BulkStringRedisSerializationProtocol() {}

    public BulkStringRedisSerializationProtocol(String cmd) {
        serialize(new String[]{cmd});
    }

    public String serialize(String[] command) {
        var bytesLength = command[0].getBytes(StandardCharsets.UTF_8).length;
        return this.serialize = type().getType() + bytesLength + END + command[0] + END;
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        try {

            var dataSize = Integer.parseInt(this.readLine(inputStream));
            if (dataSize == -1) {
                return null; // Null bulk string
            }

            byte[] read = new byte[dataSize];
            int offset = 0;
            while (offset < dataSize) {
                offset += inputStream.read(read, offset, (dataSize - offset));
            }
            // read 2 more bytes for the command delimiter
            inputStream.read();
            inputStream.read();
            return new String(read);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}