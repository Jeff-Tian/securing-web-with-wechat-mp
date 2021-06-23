package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class MpTokenManager {
    private final String weixinTokenEndpoint;

    Logger logger = LoggerFactory.getLogger(MpTokenManager.class);

    public MpTokenManager(@Value("${weixin-token-endpoint:default-test-value}") String weixinTokenEndpoint) {
        logger.info("endpoint = " + weixinTokenEndpoint, weixinTokenEndpoint);
        this.weixinTokenEndpoint = weixinTokenEndpoint;
    }

    public WeixinTokenResponse getAccessToken() {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(this.weixinTokenEndpoint);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            WeixinTokenResponse tokenResponse = new Gson().fromJson(response.body(), WeixinTokenResponse.class);
            WeixinErrorResponse errorResponse = new Gson().fromJson(response.body(), WeixinErrorResponse.class);

            if (errorResponse.errcode != 0) {
                tokenResponse.accessToken = URLEncoder.encode(errorResponse.errcode + ":" + errorResponse.errmsg);
            }

            return tokenResponse;
        } catch (Exception ex) {
            System.err.println("Exception = " + ex);
            ex.printStackTrace();

            var res = new WeixinTokenResponse();
            res.accessToken = ex.getMessage();
            return res;
        }
    }
}
