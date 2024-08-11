package kr.kkj.server.ringoredisclient.connection;

import kr.kkj.server.ringoredisclient.RedisUserContext;
import kr.kkj.server.ringoredisclient.connection.info.RedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager {

    @Getter
    private static final AccountManager instance = new AccountManager();
    private final Map<String, RedisUserContext> contextMap;

    private AccountManager() {
        contextMap = new HashMap<>();
    }

    public boolean containContext(String key){
        return contextMap.containsKey(key);
    }

    public RedisUserContext getContext(String key) {
        if ( !contextMap.containsKey(key) ) {
            throw new IllegalStateException("Socket is not open");
        }
        return contextMap.get(key);
    }


    public RedisUserContext addContext(RedisConnectionInfo redisConnectionInfo, ConnectRequest request) {
        var uuID = generateKey();
        var context = RedisUserContext
                .builder()
                .userUUID(uuID)
                .connectionInfo(redisConnectionInfo)
                .inputRequestInfo(request)
                .build();

        contextMap.put(uuID,context);
        return context;
    }

    private String generateKey() {
        var key = UUID.randomUUID().toString();
        while (contextMap.containsKey(key)){
            key = UUID.randomUUID().toString();
        }
        return key;
    }
}
