package com.uniheart.securing.web.wechat.mp.services;

import com.uniheart.securing.web.wechat.mp.Now;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WeixinQrCodeRequestPayloadTest {
    @Test
    void testSerialization() {
        var sut = new WeixinQrCodeRequestPayload();
        sut.action_info = new ActionInfo();
        sut.action_info.scene = new Scene();
        sut.action_info.scene.scene_id = 123;
        sut.action_name = "QR_SCENE";
        sut.expire_seconds = 604800;

        assertThat(sut.toJson()).isEqualTo("{\"action_name\":\"QR_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":123}},\"expire_seconds\":604800}");
    }

    @Test
    void testRandomInstance() {
        Now.setClock(
                Clock.fixed(
                        Instant.parse( "2016-01-23T12:34:56Z"), ZoneOffset.UTC
                )
        );

        var sut = WeixinQrCodeRequestPayload.getRandomInstance();
        assertThat(sut.toJson()).isEqualTo("{\"action_name\":\"QR_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":1453552496}},\"expire_seconds\":604800}");
    }
}

