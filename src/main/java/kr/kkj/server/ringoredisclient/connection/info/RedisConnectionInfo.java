package kr.kkj.server.ringoredisclient.connection.info;

import java.net.Socket;
import java.util.List;

public interface RedisConnectionInfo {

    Socket getBaseSocket();
    void close();
    Object sendCommand(String[] command);
    Object sendGroupCommand(List<String[]> groupCommands);
    List<ClusterRedisConnectionInfo.HostInfo> getAllNodes();
    ClusterRedisConnectionInfo.HostInfo getNode(String nodeID);
    SimpleRedisConnectionInfo getConnectionInfoBy(String key);
    boolean isCluster();
}
