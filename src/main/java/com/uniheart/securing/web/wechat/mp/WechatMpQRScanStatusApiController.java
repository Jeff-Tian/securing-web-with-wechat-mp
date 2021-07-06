package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpMessageService;
import com.uniheart.wechatmpservice.api.MpQrScanStatusApi;
import com.uniheart.wechatmpservice.models.MpQRScanStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class WechatMpQRScanStatusApiController implements MpQrScanStatusApi {
    private final MpMessageService mpMessageService;

    public WechatMpQRScanStatusApiController(MpMessageService mpMessageService) {
        this.mpMessageService = mpMessageService;
    }

    @Override
    public ResponseEntity<MpQRScanStatus> mpQrScanStatus(String ticket) {
        return new ResponseEntity<>(new MpQRScanStatus().openId("1234"), HttpStatus.OK);
    }
}
