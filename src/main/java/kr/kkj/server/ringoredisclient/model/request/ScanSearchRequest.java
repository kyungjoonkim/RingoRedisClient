package kr.kkj.server.ringoredisclient.model.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Builder
public class ScanSearchRequest {
    private String searchValue;
    private int count;
    private int cursor;


    public boolean isEmptySearch(){
        return !StringUtils.hasText(searchValue);
    }

    public String getCursorStr(){
        return String.valueOf(this.cursor);
    }

    public String getCountStr(){
        return String.valueOf(this.count);
    }


}
