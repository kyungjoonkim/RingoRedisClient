package kr.kkj.server.ringoredisclient.type;

import kr.kkj.server.ringoredisclient.connection.info.SimpleRedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ScanNodeInfo;
import kr.kkj.server.ringoredisclient.model.response.RetrievedKeyResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GettingDataByRedisTypeFactory {

    private static final Map<RedisType, RedisTypeGetter> getterMap;
    static {
        getterMap = Map.of(
                RedisType.String, new RedisStringTypeGetter(),
                RedisType.List, new RedisListTypeGetter(),
                RedisType.Set, new RedisSetTypeGetter(),
                RedisType.Zset, new RedisZsetTypeGetter(),
                RedisType.Hash, new RedisHashTypeGetter(),
                RedisType.Stream, new RedisStreamTypeGetter()
        );

    }

    public static RetrievedKeyResponse getData(RedisType redisType,
                               SimpleRedisConnectionInfo redisConnectionInfo,
                               ScanNodeInfo scanNodeInfo) {

         return getterMap.getOrDefault(redisType,new RedisNoneTypeGetter()).get(redisConnectionInfo,scanNodeInfo);

    }


    public interface RedisTypeGetter{
        RedisType getType();
        RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo);
    }

    public static class RedisNoneTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.None;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            throw new RuntimeException("None Redis Type");
        }
    }

    public static class RedisStringTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.String;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            String result = (String) redisConnectionInfo.sendCommand(new String[]{"GET",scanNodeInfo.getRedisKey()});
            return RetrievedKeyResponse
                    .builder()
                    .values(List.of(result))
                    .key(scanNodeInfo.getRedisKey())
                    .redisType(RedisType.String.getRedisType())
                    .build();
        }
    }
    public static class RedisListTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.List;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            var result = (List<String>) redisConnectionInfo.sendCommand(new String[]{"LRANGE",scanNodeInfo.getRedisKey(),scanNodeInfo.getStrCursor(),scanNodeInfo.getStrCount()});
            return RetrievedKeyResponse
                    .builder()
                    .values(result)
                    .key(scanNodeInfo.getRedisKey())
                    .cursor(result.size() >= scanNodeInfo.getCount() ? scanNodeInfo.getCursor() + result.size() : 0)
                    .redisType(RedisType.List.getRedisType())
                    .build();

        }
    }
    public static class RedisSetTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.Set;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            var result = (List<Object>) redisConnectionInfo.sendCommand(new String[]{"SSCAN",scanNodeInfo.getRedisKey(),scanNodeInfo.getStrCursor(),"COUNT",scanNodeInfo.getStrCount()});
            return RetrievedKeyResponse
                    .builder()
                    .values((List<String>) result.getLast())
                    .key(scanNodeInfo.getRedisKey())
                    .cursor(Integer.parseInt((String) result.getFirst()))
                    .redisType(RedisType.Set.getRedisType())
                    .build();
        }
    }
    public static class RedisZsetTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.Zset;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            var cmd = new String[]{"ZSCAN",scanNodeInfo.getRedisKey(),scanNodeInfo.getStrCursor(),"COUNT",scanNodeInfo.getStrCount()};
            var result = (List<Object>) redisConnectionInfo.sendCommand(cmd);

            Map<String, String> resultMap = new HashMap<>();
            if( result.size() > 1 ){
                List<String> dataList = (List<String>) result.get(1);
                resultMap = IntStream.range(0, dataList.size() / 2)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> dataList.get(i * 2),
                                i -> dataList.get(i * 2 + 1)
                        ));
            }
            return RetrievedKeyResponse
                    .builder()
                    .cursor(Integer.parseInt((String)result.getFirst()))
                    .fair(true)
                    .key(scanNodeInfo.getRedisKey())
                    .fairValues(resultMap)
                    .redisType(RedisType.Zset.getRedisType())
                    .build();
        }
    }
    public static class RedisHashTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.Hash;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            var cmd = new String[]{"HSCAN",scanNodeInfo.getRedisKey(),scanNodeInfo.getStrCursor(),"COUNT",scanNodeInfo.getStrCount()};
            var result = (List<Object>) redisConnectionInfo.sendCommand(cmd);

            Map<String, String> resultMap = new HashMap<>();
            if( result.size() > 1 ){
                List<String> dataList = (List<String>) result.get(1);
                resultMap = IntStream.range(0, dataList.size() / 2)
                        .boxed()
                        .collect(Collectors.toMap(
                                i -> dataList.get(i * 2),
                                i -> dataList.get(i * 2 + 1)
                        ));
            }
            return RetrievedKeyResponse
                    .builder()
                    .cursor(Integer.parseInt((String)result.getFirst()))
                    .fair(true)
                    .key(scanNodeInfo.getRedisKey())
                    .fairValues(resultMap)
                    .redisType(RedisType.Hash.getRedisType())
                    .build();
        }
    }
    public static class RedisStreamTypeGetter implements RedisTypeGetter{
        @Override
        public RedisType getType() {
            return RedisType.Stream;
        }

        @Override
        public RetrievedKeyResponse get(SimpleRedisConnectionInfo redisConnectionInfo,ScanNodeInfo scanNodeInfo) {
            return null;
        }
    }

}
