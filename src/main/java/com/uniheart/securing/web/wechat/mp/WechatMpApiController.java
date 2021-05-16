package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpService;
import com.uniheart.wechatmpservice.api.MpQrApi;
import com.uniheart.wechatmpservice.api.MpQrScanStatusApi;
import com.uniheart.wechatmpservice.models.MpQR;
import com.uniheart.wechatmpservice.models.MpQRScanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class WechatMpApiController implements MpQrApi {
    @Autowired
    private MpService mpService;

    @Override
    public ResponseEntity<MpQR> mpQrUrl() {
        return new ResponseEntity<>(this.mpService.getMpQrCode(), HttpStatus.OK);
    }
}

