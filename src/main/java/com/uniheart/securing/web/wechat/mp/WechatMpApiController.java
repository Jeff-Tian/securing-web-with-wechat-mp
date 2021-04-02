package com.uniheart.securing.web.wechat.mp;

import com.uniheart.wechatmpservice.api.MpQrApi;
import com.uniheart.wechatmpservice.models.MpQR;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class WechatMpApiController implements MpQrApi {
    @Override
    public ResponseEntity<MpQR> mpQrUrl() {
        return new ResponseEntity<>(new MpQR().ticket("test"), HttpStatus.OK);
    }
}
