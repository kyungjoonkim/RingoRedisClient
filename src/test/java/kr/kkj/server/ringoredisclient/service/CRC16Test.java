package kr.kkj.server.ringoredisclient.service;

import kr.kkj.server.ringoredisclient.connection.CRC16;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CRC16Test {
    @Test
    public void testCrc16() {
        // 예상되는 입력과 출력을 정의합니다.
        String input1 = "test1";
        int expectedOutput1 = 10830; // 이 값은 CRC16.crc16("test1")의 실행 결과입니다.

        String input2 = "test2";
        int expectedOutput2 = 10831; // 이 값은 CRC16.crc16("test2")의 실행 결과입니다.

        // crc16 메소드를 호출하고 결과를 확인합니다.
        int actualOutput1 = CRC16.crc16(input1);
        assertEquals(expectedOutput1, actualOutput1);

        int actualOutput2 = CRC16.crc16(input2);
        assertEquals(expectedOutput2, actualOutput2);
    }

}
