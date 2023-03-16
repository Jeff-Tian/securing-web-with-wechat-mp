package com.uniheart.securing.web.wechat.mp.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PulsarKeyFileTest {

    @Test
    void fromJson() {
        var json = "{\"type\":\"sn_service_account\",\"client_id\":\"0SJh0LDAZrIlbxKtIhgpyqbHazjhxZdG\",\"client_secret\":\"yyy\",\"client_email\":\"mp-message@uniheart.auth.streamnative.cloud\",\"issuer_url\":\"https://auth.streamnative.cloud/\"}";

        var keyFile = PulsarKeyFile.fromJson(json);
        assertEquals("sn_service_account", keyFile.type);
        assertEquals("yyy", keyFile.client_secret);
    }
}