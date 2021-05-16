package com.uniheart.securing.web.wechat.mp.services;

import com.uniheart.wechatmpservice.models.MpQR;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class MpService {
    private final HttpClient httpClient;

    public MpService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public MpQR getMpQrCode() {
        URI uri = URI.create("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN");
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString("")).uri(uri).build();

        try {
            HttpResponse response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response = " + response.body().toString());
            return new MpQR().ticket("test");
        } catch (Exception ex) {
            System.err.println("Exception = " + ex);
            ex.printStackTrace();
            return new MpQR().ticket("test");
        }
    }
}
