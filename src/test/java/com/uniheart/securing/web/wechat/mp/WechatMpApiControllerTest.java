package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import com.uniheart.securing.web.wechat.mp.services.MpTokenManager;
import com.uniheart.securing.web.wechat.mp.services.WeixinTicketResponse;
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
        mpServiceBean.setWeixinAccessTokenEndpoint(baseUrl + "/test2");

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
        var mockResponseForToken = new MockResponse();
        mockResponseForToken.setBody("{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200}");

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"ticket\":\"ticket\",\"expire_seconds\":60,\"url\":\"http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI\"}");
        mockResponse.addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponseForToken);

        mockBackEnd.enqueue(mockResponse);
        assertThat(mockBackEnd.getRequestCount()).isEqualTo(0);

        var content = this.restTemplate.getForObject("/mp-qr", WeixinTicketResponse.class);

        assertThat(mockBackEnd.getRequestCount()).isEqualTo(2);
        assertThat(content.url).isEqualTo("http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI");
        assertThat(content.ticket).isEqualTo("ticket");
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
