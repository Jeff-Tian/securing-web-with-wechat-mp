package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;
import com.uniheart.securing.web.wechat.mp.Constants;
import com.uniheart.wechatmpservice.models.MpQR;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class MpService {
    private final HttpClient httpClient;
    private final String qrCodeCreateUrl;

    public MpService() {
        this.httpClient = HttpClient.newHttpClient();
        this.qrCodeCreateUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    }

    public MpService(HttpClient client) {
        this.httpClient = client;
        this.qrCodeCreateUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    }

    public MpService(String qrCodeCreateUrl) {
        this.qrCodeCreateUrl = qrCodeCreateUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    public MpQR getMpQrCode() {
        URI uri = URI.create(this.qrCodeCreateUrl);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString("")).uri(uri).build();

        try {
            HttpResponse response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response = " + response.body().toString());
            WeixinErrorResponse res = new Gson().fromJson(response.body().toString(), WeixinErrorResponse.class);

            if (res.errcode == (40001)) {
                return new MpQR().ticket("test").imageUrl(Constants.FALLBACK_QR_URL);
            } else {
                return new MpQR().ticket("test");
            }
        } catch (Exception ex) {
            System.err.println("Exception = " + ex);
            ex.printStackTrace();
            return new MpQR().ticket("error").imageUrl(Constants.FALLBACK_QR_URL);
        }
    }
}
