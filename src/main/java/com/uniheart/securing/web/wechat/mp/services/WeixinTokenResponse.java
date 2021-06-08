package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.annotations.SerializedName;

public class WeixinTokenResponse {
    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("expires_in")
    public String expiresInSeconds;
}
