package kr.kkj.server.ringoredisclient.connection.info;


import kr.kkj.server.ringoredisclient.componet.RedisBaseConnectionMaker;
import kr.kkj.server.ringoredisclient.componet.RedisCommandSender;
import kr.kkj.server.ringoredisclient.model.command.CommandModel;
import kr.kkj.server.ringoredisclient.protocol.parse.RedisProtocolParser;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class SimpleRedisConnectionInfo implements RedisConnectionInfo{

    public static final int INVALID_SCAN_RESPONSE_SIZE = 1;

    private final Socket socket;
    private final List<CommandModel> allCommands;
    private final Map<String,CommandModel> commadnMap;

    public SimpleRedisConnectionInfo(Socket socket) throws IOException {
        this.socket = socket;
        this.allCommands = RedisBaseConnectionMaker
                .allCommands(this.socket)
                .stream()
                .map(CommandModel::new)
                .toList();

        this.commadnMap = this.allCommands
                .stream()
                .collect(Collectors.toMap(CommandModel::getName, Function.identity()));
    }

    @Override
    public Socket getBaseSocket() {
        return socket;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object sendCommand(String[] command) {
        try {
            RedisCommandSender.sendNativeCommand(socket.getOutputStream(), command);
            return RedisProtocolParser.readResponseWithParse(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object sendGroupCommand(List<String[]> groupCommands) {
        try {
            RedisCommandSender.sendNativeMultiCommand(socket.getOutputStream(), groupCommands);
            return RedisProtocolParser.readResponseWithParse(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClusterRedisConnectionInfo.HostInfo> getAllNodes() {
        return List.of(ClusterRedisConnectionInfo
                .HostInfo.builder()
                .redisConnectionInfo(this)
                .isMaster(true)
                .nodeId(this.getBaseSocket().getInetAddress().getHostName())
                .hostName(this.getBaseSocket().getInetAddress().getHostName())
                .ip(this.getBaseSocket().getInetAddress().getHostAddress())
                .port(this.getBaseSocket().getPort())
                .build());
    }

    @Override
    public ClusterRedisConnectionInfo.HostInfo getNode(String nodeID) {
        return ClusterRedisConnectionInfo.HostInfo.builder()
                .redisConnectionInfo(this)
                .isMaster(true)
                .nodeId(this.getBaseSocket().getInetAddress().getHostName())
                .hostName(this.getBaseSocket().getInetAddress().getHostName())
                .ip(this.getBaseSocket().getInetAddress().getHostAddress())
                .port(this.getBaseSocket().getPort())
                .build();
    }

    @Override
    public SimpleRedisConnectionInfo getConnectionInfoBy(String key) {
        return this;
    }

    @Override
    public boolean isCluster() {
        return false;
    }

}
