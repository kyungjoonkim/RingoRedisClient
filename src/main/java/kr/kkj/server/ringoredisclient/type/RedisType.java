package kr.kkj.server.ringoredisclient.type;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Getter
public enum RedisType {
    None(""),
    String("string"),
    List("list"),
    Set("set"),
    Zset("zset"),
    Hash("hash"),
    Stream("stream")
    ;

    private final String redisType;
    RedisType(String redisType){
        this.redisType = redisType;
    }

    public static RedisType findRedisType(String type){
        if( !StringUtils.hasText(type) ){
            return None;
        }
        return Arrays.stream(RedisType.values())
                .filter( redisType -> redisType.getRedisType().equals(type.toLowerCase()))
                .findFirst()
                .orElse(None);

    }

    public boolean isEmptyType() {
        return None == this;
    }
}
