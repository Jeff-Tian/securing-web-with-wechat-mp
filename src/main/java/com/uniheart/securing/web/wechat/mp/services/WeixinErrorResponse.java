package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.annotations.SerializedName;

public class WeixinErrorResponse {
    @SerializedName("errcode")
    public int errcode;

    @SerializedName("errmsg")
    public String errmsg;
}
