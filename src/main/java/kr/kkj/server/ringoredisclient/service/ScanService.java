package kr.kkj.server.ringoredisclient.service;

import kr.kkj.server.ringoredisclient.RedisUserContext;
import kr.kkj.server.ringoredisclient.connection.info.ClusterRedisConnectionInfo;
import kr.kkj.server.ringoredisclient.model.request.ScanNodeInfo;
import kr.kkj.server.ringoredisclient.model.request.ScanSearchRequest;
import kr.kkj.server.ringoredisclient.model.request.ScansRequest;
import kr.kkj.server.ringoredisclient.model.response.RetrievedKeyResponse;
import kr.kkj.server.ringoredisclient.model.response.ScanResponse;
import kr.kkj.server.ringoredisclient.type.GettingDataByRedisTypeFactory;
import kr.kkj.server.ringoredisclient.type.RedisType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScanService {

    public List<ScanResponse> getScanKeys(RedisUserContext context, ScansRequest request) {
        var result = new ArrayList<ScanResponse>();
        var threads = new ArrayList<Thread>();
        var nodeInfoMap = request.getScanNodeInfoMap();
        List<ClusterRedisConnectionInfo.HostInfo> nodes;
        if(request.isInit()){
            nodes = context.getConnectionInfo().getAllNodes();
        }else {
            nodes = context
                    .getConnectionInfo()
                    .getAllNodes()
                    .stream()
                    .filter( node -> nodeInfoMap.containsKey(node.getNodeId()))
                    .toList();
        }

        if(CollectionUtils.isEmpty(nodes)){
            throw new RuntimeException("Invalid NodeInfo !!!!");
        }

        for( var node : nodes ){
            if( !node.isMaster() ){
                continue;
            }

            var nodeRequestInfo = nodeInfoMap.getOrDefault(node.getNodeId(), new ScanNodeInfo());
            if( request.isNext() && nodeRequestInfo.isEmpty()){
                continue;
            }

            threads.add(Thread.startVirtualThread(() -> {
                var cmd = new String[]{"SCAN",String.valueOf(nodeRequestInfo.getCursor()),"COUNT",request.getStrCount()};

                result.add(new ScanResponse(node,node.getRedisConnectionInfo().sendCommand(cmd)));
            }));
        }

        threads.forEach( thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }

    public RetrievedKeyResponse getRedisData(RedisUserContext context, ScanNodeInfo request){
        var targetNode = context
                .getConnectionInfo()
                .getNode(request.getNodeId());

        var type = (String) targetNode.getRedisConnectionInfo().sendCommand(new String[]{"TYPE",request.getRedisKey()});
        var redisType = RedisType.findRedisType(type);
        if( redisType.isEmptyType() ){
            throw new RuntimeException("UnKnown Redis Type");
        }
        return GettingDataByRedisTypeFactory.getData(redisType,targetNode.getRedisConnectionInfo(),request);

    }

    public void searchRedisKey(RedisUserContext context, ScanSearchRequest request) {
        var result = new ArrayList<ScanResponse>();
        var threads = new ArrayList<Thread>();
        var allNodes = context.getConnectionInfo().getAllNodes();

        for( var node : allNodes ){
            threads.add(Thread.startVirtualThread(() -> {
                var cmd = new String[]{"SCAN", request.getCursorStr(), "MATCH", request.getSearchValue(), "COUNT", request.getCountStr()};
                result.add(new ScanResponse(node,node.getRedisConnectionInfo().sendCommand(cmd)));
            }));

        }

        threads.forEach( thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
