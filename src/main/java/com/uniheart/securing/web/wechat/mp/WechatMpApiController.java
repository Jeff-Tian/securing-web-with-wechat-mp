package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import com.uniheart.wechatmpservice.api.MpQrApi;
import com.uniheart.wechatmpservice.models.MpQR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class WechatMpApiController implements MpQrApi {
    @Autowired
    private MpServiceBean mpServiceBean;

    @Override
    public ResponseEntity<MpQR> mpQrUrl() {
        return new ResponseEntity<>(this.mpServiceBean.getMpQrCode(), HttpStatus.OK);
    }


}

