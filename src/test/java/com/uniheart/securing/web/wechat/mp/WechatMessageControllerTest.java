package com.uniheart.securing.web.wechat.mp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WechatMessageControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void mpMessage() {
        var content = this.restTemplate.postForEntity("/mp-message", "", Object.class);
        assertThat(content).isNotNull();
    }
}