package kr.kkj.server.ringoredisclient.protocol.parse;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProtocolType {

    SIMPLE_STRING("+"),
    ERROR("-"),
    INTEGER(":"),
    BULK_STRING("$"),
    ARRAY("*");

    private final String type;
    ProtocolType(String type){
        this.type = type;
    }

    public static ProtocolType findType(String type){
        return Arrays.stream(ProtocolType.values())
                        .filter( protocolType -> protocolType.getType().equals(type))
                        .findFirst()
                        .orElse(ERROR);

    }




}
