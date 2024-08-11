package kr.kkj.server.ringoredisclient.protocol;

import kr.kkj.server.ringoredisclient.protocol.parse.ProtocolType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface RedisSerializationProtocol {
    String END = "\r\n";
    ProtocolType type();
    String serialize(String[] command);
    String getSerialize();
    Object deserialize(InputStream inputStream);

    default String readLine(InputStream inputStream){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = inputStream.read()) != -1) {
                if (b == '\r') {
                    int next = inputStream.read();
                    if (next != '\n') {
                        throw new IllegalArgumentException("Expected LF after CR");
                    }
                    break;
                }
                baos.write(b);
            }
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
