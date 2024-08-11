package kr.kkj.server.ringoredisclient.service;

import kr.kkj.server.ringoredisclient.RedisUserContext;
import kr.kkj.server.ringoredisclient.connection.CRC16;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import kr.kkj.server.ringoredisclient.model.request.ScanNodeInfo;
import kr.kkj.server.ringoredisclient.type.GettingDataByRedisTypeFactory;
import kr.kkj.server.ringoredisclient.type.RedisType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ConnectTests {

    @Autowired
    private ConnectionService loginService;

    public RedisUserContext loginContext = null;

    @BeforeEach
    public void setUp() throws IOException {
        var request = ConnectRequest.builder()
//                .host("172.23.0.2")
                .host("172.23.0.8")
                .port(6380)
//                .password("bitnami")
                .build();
        loginContext = loginService.connect(request);
    }

    @Test
    public void testConnectToServer()  {

        var connectionInfo = loginContext.getConnectionInfo();

        connectionInfo.sendCommand(new String[]{"SET","test","kkkk"});
        var result =  connectionInfo.sendCommand(new String[]{"GET","test"});
        System.out.println(result);

        connectionInfo.sendCommand(new String[]{"SET","test1","1111"});
        result =  connectionInfo.sendCommand(new String[]{"GET","test1"});
        System.out.println(result);

        connectionInfo.sendCommand(new String[]{"SET","test2","2222"});
        result =  connectionInfo.sendCommand(new String[]{"GET","test2"});
        System.out.println(result);

        connectionInfo.sendCommand(new String[]{"SET","test3","3333"});
        result =  connectionInfo.sendCommand(new String[]{"GET","test3"});
        System.out.println(result);

        connectionInfo.sendCommand(new String[]{"SET","test4","4444"});
        result =  connectionInfo.sendCommand(new String[]{"GET","test4"});
        System.out.println(result);

        connectionInfo.close();

    }

    @Test
    public void testMatchKeySlot()  {

        var connectionInfo = loginContext.getConnectionInfo();

        List<String> randomWords = List.of(
                "apple", "banana", "cherry", "date", "elderberry",
                "fig", "grape", "honeydew", "kiwi", "lemon",
                "mango", "nectarine", "orange", "papaya", "quince",
                "raspberry", "strawberry", "tangerine", "ugli", "vanilla",
                "watermelon", "xigua", "yam", "zucchini", "apricot",
                "blackberry", "cantaloupe", "dragonfruit", "elderflower", "feijoa",
                "grapefruit", "huckleberry", "jackfruit", "kumquat", "lime",
                "mulberry", "nutmeg", "olive", "pineapple", "quinoa",
                "radish", "spinach", "tomato", "uva", "viburnum",
                "walnut", "ximenia", "yuzu", "zinnia", "acorn",
                "buttercup", "chives", "dandelion", "echinacea", "fennel",
                "ginseng", "hibiscus", "ivy", "jasmine", "kale",
                "lavender", "mint", "nasturtium", "oregano", "parsley",
                "quassia", "rosemary", "sage", "thyme", "umbrella",
                "violet", "wisteria", "xerophyte", "yarrow", "zebrawood",
                "almond", "basil", "carrot", "dill", "endive",
                "fiddlehead", "garlic", "horseradish", "jicama", "kohlrabi",
                "lettuce", "mushroom", "navel", "onion", "parsnip",
                "quince", "rutabaga", "shallot", "turnip", "ugli",
                "vanilla", "watercress", "xylocarp", "yam", "zucchini",
                "test1","test2"
        );


        for ( var key  : randomWords){
            var slotCmd = new String[]{"CLUSTER","KEYSLOT",key};
            var serverSlotNo = (long) connectionInfo.sendCommand(slotCmd);
            int clientSlotNo = CRC16.crc16(key);

            System.out.println("key ("+ key +"): "+serverSlotNo+" , "+(serverSlotNo == clientSlotNo));
            if(serverSlotNo != clientSlotNo){
                System.out.printf( "false : " + key);
            }

        }

        connectionInfo.close();

    }

    @Test
    public void testScanCmd() throws InterruptedException {
        var connectionInfo = loginContext.getConnectionInfo();

        for(var i= 0; i< 3; i++){
//            var result = connectionInfo.sendScan(1000);
//            System.out.println(result);
        }

    }

    @Test
    public void testInsertKey(){
        var connectionInfo = loginContext.getConnectionInfo();
        int end = 200;
        for (int i = 0; i < end; i++ ){
            connectionInfo.sendCommand(new String[]{"LPUSH","LTEST","LIST_"+i});
            connectionInfo.sendCommand(new String[]{"SADD","STEST","SET_"+i});
            connectionInfo.sendCommand(new String[]{"HSET","HTEST","FIELD_"+i,"VALUE_"+i});
            connectionInfo.sendCommand(new String[]{"ZADD","ZTEST",""+i,"ZSET_"+i});
        }
    }

    @Test
    public void testScanByRedisType(){
        var connectionInfo = loginContext.getConnectionInfo();
        for( var entry : Map.of(RedisType.List,"LTEST",RedisType.Set,"STEST",RedisType.Hash,"HTEST",RedisType.Zset,"ZTEST").entrySet()){
            Object scan = GettingDataByRedisTypeFactory.getData(entry.getKey()
                    ,connectionInfo.getConnectionInfoBy(entry.getValue())
                    ,ScanNodeInfo.builder().redisKey(entry.getValue()).build());

            System.out.println(scan);
        }

    }


    @Test
    public void testSetRandomData()  {
        var connectionInfo = loginContext.getConnectionInfo();
        var randomWords = List.of(
                "apple", "banana", "cherry", "date", "elderberry",
                "fig", "grape", "honeydew", "kiwi", "lemon",
                "mango", "nectarine", "orange", "papaya", "quince",
                "raspberry", "strawberry", "tangerine", "ugli", "vanilla",
                "watermelon", "xigua", "yam", "zucchini", "apricot",
                "blackberry", "cantaloupe", "dragonfruit", "elderflower", "feijoa",
                "grapefruit", "huckleberry", "jackfruit", "kumquat", "lime",
                "mulberry", "nutmeg", "olive", "pineapple", "quinoa",
                "radish", "spinach", "tomato", "uva", "viburnum",
                "walnut", "ximenia", "yuzu", "zinnia", "acorn",
                "buttercup", "chives", "dandelion", "echinacea", "fennel",
                "ginseng", "hibiscus", "ivy", "jasmine", "kale",
                "lavender", "mint", "nasturtium", "oregano", "parsley",
                "quassia", "rosemary", "sage", "thyme", "umbrella",
                "violet", "wisteria", "xerophyte", "yarrow", "zebrawood",
                "almond", "basil", "carrot", "dill", "endive",
                "fiddlehead", "garlic", "horseradish", "jicama", "kohlrabi",
                "lettuce", "mushroom", "navel", "onion", "parsnip",
                "quince", "rutabaga", "shallot", "turnip", "ugli",
                "vanilla", "watercress", "xylocarp", "yam", "zucchini",
                "test1","test2"
        );

        for( var word : randomWords ){
            connectionInfo.sendCommand(new String[]{"SET",word,word});

        }

        connectionInfo.close();

    }

    @Test
    public void testSetRandomData2() {
        var connectionInfo = loginContext.getConnectionInfo();
//        System.out.println("LLL : "+connectionInfo.isCluster());
//
//        System.out.println(connectionInfo.sendCommand(new String[]{"GET","TEST_1",}));;
//        System.out.println(connectionInfo.sendCommand(new String[]{"SELECT","1"}));

        var groupCmds = List.of(new String[]{"SELECT","1"},new String[]{"SET","TEST_2"},new String[]{"GET","TEST_2"});
        System.out.println(connectionInfo.sendGroupCommand(groupCmds));


//        var setT = connectionInfo.sendCommand(new String[]{"SET","S1","KKK"});
//        System.out.println(data);
//
//        var setT = connectionInfo.sendCommand(new String[]{"SET","S1","KKK"});
//        System.out.println(data);

    }




}
