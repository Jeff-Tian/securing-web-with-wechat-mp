package com.uniheart.securing.web.wechat.mp.services;

import org.springframework.stereotype.Component;

@Component
public class MpTokenManager {
    public WeixinTokenResponse getAccessToken() {
        WeixinTokenResponse res = new WeixinTokenResponse();

        res.accessToken = "access";
        return res;
    }
}
