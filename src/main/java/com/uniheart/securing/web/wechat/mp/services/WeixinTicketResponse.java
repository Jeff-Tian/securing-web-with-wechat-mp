package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.annotations.SerializedName;

public class WeixinTicketResponse {
    @SerializedName("ticket")
    public String ticket;

    @SerializedName("expire_seconds")
    public long expiresInSeconds;

    @SerializedName("url")
    public String url;
}
