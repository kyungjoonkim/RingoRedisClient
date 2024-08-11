package kr.kkj.server.ringoredisclient.model.command;

import kr.kkj.server.ringoredisclient.model.response.ScanResponse;
import lombok.Getter;

@Getter
public class ScanCursor {

    private static final int END_CURSOR = 0;

    private int cursor;
    private boolean end;
    private String lastKey = "";

    public void setNextCursor(ScanResponse scanResponse){
        if(END_CURSOR == scanResponse.getCursor()){
            this.end = true;
            this.lastKey = scanResponse.getLastKey();
        }else {
            this.cursor = scanResponse.getCursor();
        }
    }

    public int getNextCursor(){
        return this.cursor;
    }


    public boolean isUpdate(ScanResponse scanResponse) {
        if( !this.isEnd() ){
            return true;
        }

        return !scanResponse.getLastKey().equals(this.lastKey);
    }
}
