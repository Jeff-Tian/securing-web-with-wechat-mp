package com.uniheart.securing.web.wechat.mp;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WechatMpApiControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Test
    void testMpUrl() {
        String jsonStr = "{\"expire_seconds\":null,\"imageUrl\":null,\"sceneId\":null,\"ticket\":\"test\",\"url\":null}";
        String content = this.restTemplate.getForObject("/mp-qr", String.class);
        assertThat(content).isEqualTo(jsonStr);
    }

    @Test
    void testEnv() {
        assertThat(env.getProperty("hello")).isEqualTo(null);
        assertThat(env.getProperty("foo")).isEqualTo(null);

        Dotenv dotenv = Dotenv.load();

        assertThat(dotenv.get("hello")).isEqualTo("world");
        assertThat(dotenv.get("foo")).isEqualTo("bar");
    }
}