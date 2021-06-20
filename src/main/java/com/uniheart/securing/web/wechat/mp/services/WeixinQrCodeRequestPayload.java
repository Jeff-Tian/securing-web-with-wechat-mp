package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;
import com.uniheart.securing.web.wechat.mp.Now;
import org.joda.time.Instant;

public class WeixinQrCodeRequestPayload {
    public String action_name;
    public ActionInfo action_info;
    public int expire_seconds;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static WeixinQrCodeRequestPayload getRandomInstance() {
        var timestamp = Now.instant();

        var ret = new WeixinQrCodeRequestPayload();
        ret.action_name = "QR_SCENE";
        ret.expire_seconds = 604800;
        ret.action_info = new ActionInfo();
        ret.action_info.scene = new Scene();
        ret.action_info.scene.scene_id = timestamp.getEpochSecond() + timestamp.getNano();

        return ret;
    }
}

class ActionInfo{
    public Scene scene;
}

class Scene {
    public long scene_id;
}
