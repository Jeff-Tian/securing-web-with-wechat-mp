package com.uniheart.securing.web.wechat.mp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WechatMpApiControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testMpUrl() {
        String jsonStr = "{\"expire_seconds\":null,\"imageUrl\":null,\"sceneId\":null,\"ticket\":\"test\",\"url\":null}";
        String content = this.restTemplate.getForObject("/mp-qr", String.class);
        assertThat(content).isEqualTo(jsonStr);
    }
}
