package com.uniheart.securing.web.wechat.mp;

import com.uniheart.wechatmpservice.models.Xml;
import org.apache.http.HttpEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WechatMessageControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void mpMessage() {
        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");

        var req = new org.springframework.http.HttpEntity<>(new Xml(), headers);

        var content = this.restTemplate.postForEntity("/mp-message", req, Object.class);
        assertThat(content).isNotNull();
        assertThat(content.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}