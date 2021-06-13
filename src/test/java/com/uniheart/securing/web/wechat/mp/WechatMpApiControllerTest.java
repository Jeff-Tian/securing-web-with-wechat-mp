package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import com.uniheart.securing.web.wechat.mp.services.MpTokenManager;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WechatMpApiControllerTest {
    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());

        mpServiceBean.setQrCodeCreateUrl(baseUrl + "/test");

        mpTokenManager = new MpTokenManager(baseUrl + "/token");
    }

    @Autowired
    private MpServiceBean mpServiceBean;

    @Autowired
    private MpTokenManager mpTokenManager;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Test
    void testMpUrlHappyPath() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"ticket\":\"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm\n" +
                "3sUw==\",\"expire_seconds\":60,\"url\":\"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI\"}");
        mockResponse.addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponse);
        assertThat(mockBackEnd.getRequestCount()).isEqualTo(0);

        String jsonStr = "{\"expire_seconds\":60,\"imageUrl\":\"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI\",\"sceneId\":null,\"ticket\":\"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm\\n3sUw==\",\"url\":\"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI\"}";
        String content = this.restTemplate.getForObject("/mp-qr", String.class);

        assertThat(mockBackEnd.getRequestCount()).isEqualTo(1);
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
