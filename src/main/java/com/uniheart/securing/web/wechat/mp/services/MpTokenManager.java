package com.uniheart.securing.web.wechat.mp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MpTokenManager {
    private final String weixinTokenEndpoint;

    public MpTokenManager(@Value("${weixin-token-endpoint}") String weixinTokenEndpoint){
        this.weixinTokenEndpoint = weixinTokenEndpoint;
    }

    public WeixinTokenResponse getAccessToken() {
        WeixinTokenResponse res = new WeixinTokenResponse();

        res.accessToken = "access";
        return res;
    }
}
