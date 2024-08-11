package kr.kkj.server.ringoredisclient.error.exception;

import kr.kkj.server.ringoredisclient.Const;

public class InvalidConnectionException extends RuntimeException{

    public InvalidConnectionException() {
        super(Const.ErrorMessages.INVALID_CONNECTION);
    }

}
