package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItGatewayTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testMainMethod() {
        ShareItGateway.main(new String[] {});
    }
}
