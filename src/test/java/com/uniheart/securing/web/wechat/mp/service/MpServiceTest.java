package com.uniheart.securing.web.wechat.mp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniheart.securing.web.wechat.mp.services.MpService;
import com.uniheart.wechatmpservice.models.MpQR;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MpServiceTest {
    private MpService mpService = new MpService(new MockHttpClient());

    @Test
    void testGetMpQrCode() {
        MpQR mpQR = mpService.getMpQrCode();
        assertThat(mpQR.getTicket()).isEqualTo("test");
    }

    @Test
    void testConvertJsonToObject() {
        String json = "{\"errcode\":40001,\"errmsg\":\"invalid credential, access_token is invalid or not latest rid: 60a256b1-0d6a1940-3e617241\"}";

        assertThatNoException().isThrownBy(() -> {
            WeixinError error = new ObjectMapper().readValue(json, WeixinError.class);
            assertThat(error.errcode).isEqualTo(40001);
        });
    }
}