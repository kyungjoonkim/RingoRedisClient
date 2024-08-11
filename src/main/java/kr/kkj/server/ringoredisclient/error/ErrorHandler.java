package kr.kkj.server.ringoredisclient.error;


import kr.kkj.server.ringoredisclient.error.exception.InvalidConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvalidConnectionException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidConnectionException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }



}
