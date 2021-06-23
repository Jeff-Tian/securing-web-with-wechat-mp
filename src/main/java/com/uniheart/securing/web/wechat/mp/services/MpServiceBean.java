package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uniheart.securing.web.wechat.mp.Constants;
import com.uniheart.wechatmpservice.models.MpQR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UnknownFormatConversionException;

@Component
public class MpServiceBean {
    private HttpClient httpClient;

    @Value("${weixin-qr-code-creation-endpoint:default-test-value}")
    private String qrCodeCreateUrl;

    @Value("${weixin-token-endpoint:default-test-value}")
    private String weixinAccessTokenEndpoint;

    public String getQrCodeCreateUrl() {
        return this.qrCodeCreateUrl;
    }

    public MpServiceBean() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public MpServiceBean(HttpClient client, String qrCodeCreateUrl, String tokenEndpoint) {
        this.httpClient = client;
        this.qrCodeCreateUrl = qrCodeCreateUrl;
        this.weixinAccessTokenEndpoint = tokenEndpoint;
    }

    public void setQrCodeCreateUrl(String url) {
        this.qrCodeCreateUrl = url;
    }

    public void setWeixinAccessTokenEndpoint(String url) {
        this.weixinAccessTokenEndpoint = url;
    }

    Logger logger = LoggerFactory.getLogger(MpServiceBean.class);

    public MpQR getMpQrCode() {
        var mpTokenManager = new MpTokenManager(this.weixinAccessTokenEndpoint);

        URI uri = URI.create(this.qrCodeCreateUrl + mpTokenManager.getAccessToken().accessToken);

        logger.info("Getting qr code with " + uri);

        HttpRequest request =
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(WeixinQrCodeRequestPayload.getRandomInstance().toJson())).uri(uri).build();

        try {
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            WeixinErrorResponse errorResponse = new Gson().fromJson(response.body(), WeixinErrorResponse.class);
            WeixinTicketResponse ticketResponse = new Gson().fromJson(response.body(), WeixinTicketResponse.class);

            if (ticketResponse.ticket != null) {
                return new MpQR().ticket(ticketResponse.ticket).imageUrl(ticketResponse.url).expireSeconds(ticketResponse.expiresInSeconds).url(ticketResponse.url);
            }

            if (errorResponse.errcode == (40001)) {
                return new MpQR().ticket("test").imageUrl(Constants.FALLBACK_QR_URL);
            }

            throw new UnknownFormatConversionException(response.body());
        } catch (InterruptedException ie) {
            System.err.println("Exception = " + ie);
            ie.printStackTrace();

            return new MpQR().ticket("interrupted").imageUrl(Constants.FALLBACK_QR_URL);
        } catch (Exception ex) {
            System.err.println("Exception = " + ex);
            ex.printStackTrace();
            return new MpQR().ticket("error").imageUrl(Constants.FALLBACK_QR_URL);
        }
    }
}
