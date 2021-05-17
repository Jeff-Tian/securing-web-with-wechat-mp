package com.uniheart.securing.web.wechat.mp.service;

import com.uniheart.securing.web.wechat.mp.services.MpService;
import com.uniheart.wechatmpservice.models.MpQR;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MpServiceTest {
    @Autowired
    private MpService mpService;

    @Test
    void testGetMpQrCode() {
        MpQR mpQR = mpService.getMpQrCode();
        assertThat(mpQR.getTicket()).isEqualTo("test");
    }
}