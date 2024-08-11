package kr.kkj.server.ringoredisclient.componet;

import kr.kkj.server.ringoredisclient.protocol.parse.RedisProtocolParser;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class RedisBaseConnectionMaker {

    private static final String AUTH_COMMAND = "AUTH";
    private static final String SUCCESS_CODE = "OK";

    private static final String[] CLUSTER_COMMAND = new String[]{"CLUSTER","INFO"};
    private static final String[] ALL_COMMAND = new String[]{"COMMAND"};
    private static final String CLUSTER_OK = "cluster_state:ok";

    public static Socket generateRedisSocket(ConnectRequest inputRedisInfo) throws IOException {
        var socket = new Socket(inputRedisInfo.getHost(), inputRedisInfo.getPort());
        socket.setSoTimeout(5000);

        if(inputRedisInfo.isAuth() && !isAuthOK(socket, inputRedisInfo)){
            throw new RuntimeException("Auth Fail");
        }
        return socket;
    }

    private static String[] makeAuthCommand(ConnectRequest inputRedisInfo) {
        if(inputRedisInfo.emptyUserName()){
            return new String[]{AUTH_COMMAND,inputRedisInfo.getPassword()};
        }
        return new String[]{AUTH_COMMAND,inputRedisInfo.getUserName(),inputRedisInfo.getPassword()};
    }

    private static boolean isAuthOK(Socket socket, ConnectRequest inputRedisInfo) throws IOException {
        RedisCommandSender.sendNativeCommand(socket.getOutputStream(), makeAuthCommand(inputRedisInfo));
        var authResult = (String) RedisProtocolParser.readResponseWithParse(socket.getInputStream());
        return SUCCESS_CODE.equals(authResult);
    }

    public static boolean isRedisCluster(Socket socket) throws IOException {
        RedisCommandSender.sendNativeCommand(socket.getOutputStream(), CLUSTER_COMMAND);
        try{
            var response = (String) RedisProtocolParser.readResponseWithParse(socket.getInputStream());
            return response.contains(CLUSTER_OK);
        }catch (Exception e){
            return false;
        }
//        var response = (String) RedisProtocolParser.readResponseWithParse(socket.getInputStream());
//        return response.contains(CLUSTER_OK);

    }

    public static List<Object> allCommands(Socket socket) throws IOException {
        RedisCommandSender.sendNativeCommand(socket.getOutputStream(), ALL_COMMAND);
        return (List) RedisProtocolParser.readResponseWithParse(socket.getInputStream());

    }

}
