package com.uniheart.securing.web.wechat.mp.services;

import com.uniheart.wechatmpservice.models.MpQR;
import org.springframework.stereotype.Component;

@Component
public class MpService {
    public MpQR getMpQrCode() {
        return new MpQR().ticket("test");
    }
}
