package kr.kkj.server.ringoredisclient.api;

import kr.kkj.server.ringoredisclient.model.request.ConnectRequest;
import kr.kkj.server.ringoredisclient.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

@AutoConfigureMockMvc
@SpringBootTest
class ScanControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ConnectionService loginService;

//    public RedisUserContext loginContext = null;

    @BeforeEach
    public void setUp() throws IOException {
//        var request = ConnectRequest.builder()
//                .host("172.23.0.2")
//                .port(6379)
//                .password("bitnami")
//                .build();
//        loginContext = loginService.connect(request);
    }


    @Test
    public void testScanList() throws Exception {
        var redisConnect = loginService.connect(ConnectRequest.builder()
                .host("172.23.0.2")
                .port(6379)
                .password("bitnami")
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/scan/list")
                        .param("id", redisConnect.getUserUUID())

                ).andDo(h -> System.out.println(h.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk()); // HTTP 상태 코드가 200인지 확인합니다.
    }

}