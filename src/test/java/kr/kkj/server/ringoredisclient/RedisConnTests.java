package kr.kkj.server.ringoredisclient;

import kr.kkj.server.ringoredisclient.componet.RedisBaseConnectionMaker;
import kr.kkj.server.ringoredisclient.connection.info.SimpleRedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import kr.kkj.server.ringoredisclient.type.GettingDataByRedisTypeFactory;
import kr.kkj.server.ringoredisclient.type.RedisType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedisConnTests {

    @Test
    @Disabled
    public void testConnectToServer() throws IOException {
        String str = "*2\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+Hello\r\n-World\r\n";







//        BufferedReader reader = new BufferedReader(new StringReader(str));
//        var result = RedisProtocolParser.readResponseWithParse(reader);
//        RedisProtocolParser.printOf(result);
    }


    @Test
    public void testJedis(){

        JedisShardInfo info = new JedisShardInfo("172.23.0.3",6379);
        info.setPassword("bitnami");

        HostAndPort hostAndPort = new HostAndPort("172.23.0.3",6379);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisCluster cluster = new JedisCluster(hostAndPort, 2000, 2000, 5, "bitnami", poolConfig);

//        System.out.println(jedis.get("test11"));
        for (int i =0; i < 1; i++){
            System.out.println("=====================================");
            scanAllNodes(cluster);
        }

    }

    private void scanAllNodes(JedisCluster cluster) {
        List<String> result = new ArrayList<>();
        for (JedisPool node : cluster.getClusterNodes().values()) {
            try (Jedis j = node.getResource()) {
                var keys = scanNode(j); // Single node scan from earlier example
                result.addAll(keys);
            }
        }
        System.out.println(result);
    }

    private List<String> scanNode(Jedis node) {

        List<String> result = new ArrayList<>();
        ScanParams scanParams = new ScanParams().count(1000);
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> scanResult = node.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            result.addAll(keys);;
            cursor = scanResult.getCursor();
        } while (!cursor.equals(ScanParams.SCAN_POINTER_START));

        return result;
    }



    @Test
    public void testRedisLocal() throws IOException {
        var baseSocket = RedisBaseConnectionMaker
                .generateRedisSocket(ConnectRequest
                        .builder()
                        .host("172.23.0.2")
                        .port(6380)
                        .build());

        SimpleRedisConnectionInfo redisConnectionInfo = new SimpleRedisConnectionInfo(baseSocket);


        var setCommand = new String[]{"SET", "test", "하이"};
        redisConnectionInfo.sendCommand(setCommand);

        var getCommand = new String[]{"GET", "test"};
        var result = redisConnectionInfo.sendCommand(getCommand);

        System.out.println(result);

//        String str = "테스트";
//        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
//        int length = bytes.length;
//        System.out.println("Length of the string in UTF-8: " + length);
    }



}
