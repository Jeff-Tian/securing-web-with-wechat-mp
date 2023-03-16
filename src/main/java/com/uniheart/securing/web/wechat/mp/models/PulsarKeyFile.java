package com.uniheart.securing.web.wechat.mp.models;

import org.apache.pulsar.shade.com.google.gson.Gson;

public class PulsarKeyFile {
    public String type;
    public String client_id;
    public String client_secret;
    public String client_email;
    public String issuer_url;

    public static PulsarKeyFile fromJson(String json) {
        return new Gson().fromJson(json, PulsarKeyFile.class);
    }
}
