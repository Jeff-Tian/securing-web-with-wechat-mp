package com.uniheart.securing.web.wechat.mp.services;

import org.junit.jupiter.api.Test;

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
}