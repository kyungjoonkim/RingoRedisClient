package kr.kkj.server.ringoredisclient.model.request;

import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectRequest {
    private String host;
    private int port;
    private String userName;
    private String password;
    private boolean docker;

    public boolean inValid() {
        return StringUtils.isEmpty(host) || port <=0;
    }

    public boolean emptyUserName() {
        return StringUtils.isEmpty(userName);
    }

    public boolean isNonAuth() {
        return emptyUserName() && StringUtils.isEmpty(password);
    }

    public boolean isAuth() {
        return !isNonAuth();
    }


    public ConnectRequest makeSlotRequest(String host,int port) {
        if(docker){
            host = "localhost";
        }
        return ConnectRequest.builder()
                .host(host)
                .port(port)
                .userName(userName)
                .password(password)
                .docker(docker)
                .build();
    }

}
