package com.uniheart.securing.web.wechat.mp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import com.uniheart.wechatmpservice.models.MpQR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MpServiceBeanTest {
    private MpServiceBean mpServiceBean = new MpServiceBean(new MockHttpClient());

    @Test
    void testGetMpQrCode() {
        MpQR mpQR = mpServiceBean.getMpQrCode();
        assertThat(mpQR.getTicket()).isEqualTo("error");
    }

    @Test
    void testConvertJsonToObject() {
        String json = "{\"errcode\":40001,\"errmsg\":\"invalid credential, access_token is invalid or not latest rid: 60a256b1-0d6a1940-3e617241\"}";

        assertThatNoException().isThrownBy(() -> {
            WeixinError error = new ObjectMapper().readValue(json, WeixinError.class);
            assertThat(error.errcode).isEqualTo(40001);
        });
    }

    @Test
    void testGetMpQrCodeMetInterruptedException() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(), any())).thenThrow(new InterruptedException("Test Exception"));

        mpServiceBean = new MpServiceBean(mockHttpClient);
        MpQR mpQR = mpServiceBean.getMpQrCode();
        assertThat(mpQR.getTicket()).isEqualTo("interrupted");
    }

    @Mock
    private HttpClient mockHttpClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(MpServiceBeanTest.class);
    }
}