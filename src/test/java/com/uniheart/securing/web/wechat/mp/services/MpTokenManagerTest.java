package com.uniheart.securing.web.wechat.mp.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MpTokenManagerTest {
    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    private MpTokenManager mpTokenManager;

    @Test
    void testGetAccessTokenFail() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"errcode\":40013,\"errmsg\":\"invalid appid\"}");
        mockResponse.addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponse);
        assertThat(mockBackEnd.getRequestCount()).isEqualTo(0);

        this.mpTokenManager = new MpTokenManager(String.format("http://localhost:%s",
                mockBackEnd.getPort()));

        var res = this.mpTokenManager.getAccessToken();

        assertThat(mockBackEnd.getRequestCount()).isEqualTo(1);
        assertThat(res.accessToken).isEqualTo("40013:invalid appid");
    }

    @Test
    void testGetAccessTokenSuccess() {
        var mockResponse = new MockResponse();
        mockResponse.setBody("{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200}");
        mockResponse.addHeader("Content-Type", "application/json");

        mockBackEnd.enqueue(mockResponse);

        this.mpTokenManager = new MpTokenManager(String.format("http://localhost:%s", mockBackEnd.getPort()));

        var res = this.mpTokenManager.getAccessToken();

        assertThat(res.accessToken).isEqualTo("ACCESS_TOKEN");
    }
}