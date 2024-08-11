package kr.kkj.server.ringoredisclient.api;

import kr.kkj.server.ringoredisclient.error.exception.InvalidConnectionException;
import kr.kkj.server.ringoredisclient.model.response.ResponseLogin;
import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import kr.kkj.server.ringoredisclient.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final ConnectionService connectionService;
//    private final ScanService scanService;

    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody ConnectRequest request) {
        if(request.inValid()){
            throw new InvalidConnectionException();
        }

        try {
            var context = connectionService.connect(request);
            var response = ResponseLogin
                    .builder()
                    .uuid(context.getUserUUID())
                    .success(StringUtils.hasText(context.getUserUUID()))
                    .build();

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


}
