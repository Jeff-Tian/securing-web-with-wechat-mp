package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;

public class WeixinQrCodeRequestPayload {
    public String action_name;
    public ActionInfo action_info;
    public int expire_seconds;

    public String toJson() {
        return new Gson().toJson(this);
    }
}

class ActionInfo{
    public Scene scene;
}

class Scene {
    public int scene_id;
}
