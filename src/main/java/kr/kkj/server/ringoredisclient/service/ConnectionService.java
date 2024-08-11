package kr.kkj.server.ringoredisclient.service;

import kr.kkj.server.ringoredisclient.RedisUserContext;
import kr.kkj.server.ringoredisclient.componet.RedisBaseConnectionMaker;
import kr.kkj.server.ringoredisclient.connection.AccountManager;
import kr.kkj.server.ringoredisclient.connection.info.ClusterRedisConnectionInfo;
import kr.kkj.server.ringoredisclient.connection.info.RedisConnectionInfo;
import kr.kkj.server.ringoredisclient.connection.info.SimpleRedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    public RedisUserContext connect(ConnectRequest request) throws IOException {
        var baseSocket = RedisBaseConnectionMaker.generateRedisSocket(request);

        RedisConnectionInfo redisConnectionInfo = null;
        if(RedisBaseConnectionMaker.isRedisCluster(baseSocket)){
            redisConnectionInfo = new ClusterRedisConnectionInfo(baseSocket,request);
        }else{
            redisConnectionInfo = new SimpleRedisConnectionInfo(baseSocket);
        }

        return AccountManager.getInstance().addContext(redisConnectionInfo,request);
    }


}
