package com.uniheart.securing.web.wechat.mp.services;

import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import com.google.gson.Gson;
import com.uniheart.securing.web.wechat.mp.Constants;
import com.uniheart.wechatmpservice.models.MpQR;
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

    @Value("${weixin-qr-code-creation-endpoint:test}")
    private String qrCodeCreateUrl;

    public String getQrCodeCreateUrl()
    {
        return this.qrCodeCreateUrl;
    }

    public MpServiceBean() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public MpServiceBean(HttpClient client) {
        this.httpClient = client;
    }

    public MpServiceBean(String qrCodeCreateUrl) {
        this.qrCodeCreateUrl = qrCodeCreateUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    public void setQrCodeCreateUrl(String url) {
        this.qrCodeCreateUrl = url;
    }

    public MpQR getMpQrCode() {
        URI uri = URI.create(this.qrCodeCreateUrl);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString("")).uri(uri).build();

        try {
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            WeixinErrorResponse errorResponse = new Gson().fromJson(response.body(), WeixinErrorResponse.class);
            WeixinTicketResponse ticketResponse = new Gson().fromJson(response.body(), WeixinTicketResponse.class);

            if (!ticketResponse.ticket.equals("")) {
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
