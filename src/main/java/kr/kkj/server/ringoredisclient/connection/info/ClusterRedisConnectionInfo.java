package kr.kkj.server.ringoredisclient.connection.info;

import kr.kkj.server.ringoredisclient.componet.RedisBaseConnectionMaker;
import kr.kkj.server.ringoredisclient.componet.RedisCommandSender;
import kr.kkj.server.ringoredisclient.connection.CRC16;
import kr.kkj.server.ringoredisclient.model.command.CommandModel;
import kr.kkj.server.ringoredisclient.protocol.parse.RedisProtocolParser;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ClusterRedisConnectionInfo implements RedisConnectionInfo{

    private static final String[] CLUSTER_SLOT_COMMAND = new String[]{"CLUSTER","SLOTS"};
    private static final String[] KEY_SLOT_COMMAND = new String[]{"CLUSTER","KEYSLOT"};

    private final ConnectRequest baseConnectRequest;
    private final Socket baseSocket;
    private final SimpleRedisConnectionInfo baseConnectionInfo;
    private final List<SlotInfo> slotNodeInfos;

    public ClusterRedisConnectionInfo(Socket baseSocket,ConnectRequest baseConnectRequest) throws IOException {
        this.baseSocket = baseSocket;
        this.baseConnectionInfo = new SimpleRedisConnectionInfo(baseSocket);
        this.baseConnectRequest = baseConnectRequest;
        this.slotNodeInfos = getClusterSlotInfos();

    }

    @Override
    public boolean isCluster() {
        return true;
    }

    //슬롯 정보
    private List<SlotInfo> getClusterSlotInfos() throws IOException {
        RedisCommandSender.sendNativeCommand(baseSocket.getOutputStream(), CLUSTER_SLOT_COMMAND);
        var nativeNodeInfos = (List<Object>) RedisProtocolParser.readResponseWithParse(baseSocket.getInputStream());

        return nativeNodeInfos.stream().map(nodeInfo -> {
            List<Object> targetNodeInfos = (List<Object>) nodeInfo;
            if(targetNodeInfos.isEmpty()){
                return SlotInfo.builder().build();
            }
            return SlotInfo
                    .builder()
                    .startSlot((long) targetNodeInfos.get(SlotInfo.START_SLOT_INDEX))
                    .endSlot((long) targetNodeInfos.get(SlotInfo.END_SLOT_INDEX))
                    .hostInfos(hostInfos(targetNodeInfos))
                    .build();
        }).toList();
    }
    //호스트 정보
    private List<HostInfo> hostInfos(List<Object> hosts){
        if(hosts.size() <= SlotInfo.HOST_INFO_INDEX){
            return new ArrayList<>();
        }

        List<HostInfo> result = new ArrayList<>();
        for(int hostInfoIndex = SlotInfo.HOST_INFO_INDEX; hostInfoIndex < hosts.size(); hostInfoIndex++){
            var hostInfos = (List) hosts.get(hostInfoIndex);
            if(CollectionUtils.isEmpty(hostInfos)){
                continue;
            }


            var ip = (String) hostInfos.get(HostInfo.IP_INDEX);
            var port = (long) hostInfos.get(HostInfo.PORT_INDEX);
            SimpleRedisConnectionInfo simpleRedisConnectionInfo = null;

            try {
               var socket = RedisBaseConnectionMaker
                        .generateRedisSocket(baseConnectRequest.makeSlotRequest(ip,(int)port));
                simpleRedisConnectionInfo = new SimpleRedisConnectionInfo(socket);

            } catch (IOException e) {}

            result.add(HostInfo.builder()
                    .isMaster(SlotInfo.HOST_INFO_INDEX == hostInfoIndex)
                    .ip(ip)
                    .port(port)
                    .redisConnectionInfo(simpleRedisConnectionInfo)
                    .nodeId(hostInfos.size() > HostInfo.NODE_ID_INDEX ? (String) hostInfos.get(HostInfo.NODE_ID_INDEX) : "")
                    .hostName(getHostName(hostInfos))
                    .build());
        }
        return result;
    }

    //4번 HostName 정보가 있을 경우
    private String getHostName(List<Object> hostInfos){
        if(hostInfos.size() <= HostInfo.NODE_ID_INDEX){
            return "";
        }

        var hostNameInfos = (List)hostInfos.get(HostInfo.HOST_NAME_INDEX);
        if (CollectionUtils.isEmpty(hostNameInfos)
                || hostNameInfos.size() < HostNameInfo.HOST_NAME_INDEX + 1) {
            return "";

        }
        return  (String) hostNameInfos.get(HostNameInfo.HOST_NAME_INDEX);
    }

    @Override
    public void close() {
        try {
            baseSocket.close();

            if(slotNodeInfos.isEmpty()){
                return;
            }

            slotNodeInfos.forEach(slotInfo -> {
                if(slotInfo.getHostInfos().isEmpty()){
                    return;
                }
                slotInfo.getHostInfos().forEach(hostInfo -> {
                    hostInfo.getRedisConnectionInfo().close();
                });
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object sendCommand(String[] commands) {
        var commadnMap = baseConnectionInfo.getCommadnMap();

        var command = commands[0].toLowerCase();
        var commandInfo = commadnMap.getOrDefault(command,new CommandModel());
        if(commandInfo.emptyCommand()){
            throw new RuntimeException("Not found command : " + command);
        }

        if(commandInfo.invalidCommand(commands)){
            throw new RuntimeException("Invalid command : " + command);
        }

        String key = commandInfo.findFirstKey(commands);
        var slotValue = CRC16.crc16(key);

        SlotInfo targetSlotInfo = slotNodeInfos.stream()
                .filter(slotInfo -> slotInfo.getStartSlot() <= slotValue && slotValue <= slotInfo.getEndSlot())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found slot : " + slotValue));

        return targetSlotInfo.getHostInfos().getFirst().getRedisConnectionInfo().sendCommand(commands);
    }

    @Override
    public Object sendGroupCommand(List<String[]> groupCommands) {
        return null;
    }

    @Override
    public List<HostInfo> getAllNodes() {
        return slotNodeInfos
                .stream()
                .map(SlotInfo::getHostInfos)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public HostInfo getNode(String nodeID) {
       var targetSlot = slotNodeInfos
                .stream()
                .filter( node -> node.existHostInfo(nodeID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found NodeID : " + nodeID));

        return targetSlot.getHostInfos().getFirst();
    }

    @Override
    public SimpleRedisConnectionInfo getConnectionInfoBy(String key) {
        var slotValue = CRC16.crc16(key);

        SlotInfo targetSlotInfo = slotNodeInfos.stream()
                .filter(slotInfo -> slotInfo.getStartSlot() <= slotValue && slotValue <= slotInfo.getEndSlot())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found slot : " + slotValue));

        return targetSlotInfo.getHostInfos().getFirst().getRedisConnectionInfo();
    }

    @Getter
    @Builder
    public static class SlotInfo{
        private static final int START_SLOT_INDEX = 0;
        private static final int END_SLOT_INDEX = 1;
        private static final int HOST_INFO_INDEX = 2;

        private long startSlot;
        private long endSlot;
        private List<HostInfo> hostInfos;

        public boolean existHostInfo(String nodeID) {
            if(CollectionUtils.isEmpty(hostInfos)){
                return false;
            }
            return hostInfos
                    .stream()
                    .anyMatch( hostInfo -> hostInfo.isMatchNodeId(nodeID));
        }
    }

    @Getter
    @Builder
    public static class HostInfo {
        public static final int  IP_INDEX = 0;
        public static final int  PORT_INDEX = 1;
        public static final int NODE_ID_INDEX = 2;
        public static final int HOST_NAME_INDEX = 3;

        private boolean isMaster;
        private SimpleRedisConnectionInfo redisConnectionInfo;
        private String ip;
        private long port;
        private String nodeId;
        private String hostName;

       public boolean isMatchNodeId(String nodeID){
           if(StringUtils.hasText(this.nodeId)){
               return this.nodeId.equals(nodeID);
           }
           return false;
       }
    }


    public static class HostNameInfo {
        public static final int  HOST_NAME_INDEX = 1;
    }





}
