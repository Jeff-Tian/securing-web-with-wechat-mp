package com.uniheart.securing.web.wechat.mp.services;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.uniheart.securing.web.wechat.mp.models.PulsarKeyFile;
import com.uniheart.wechatmpservice.models.Xml;
import org.apache.pulsar.client.impl.auth.oauth2.AuthenticationOAuth2;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.apache.pulsar.client.api.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.auth.oauth2.AuthenticationFactoryOAuth2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class MpMessageService {
    Logger logger = LoggerFactory.getLogger(MpMessageService.class);

    private final String pulsarUrl;
    private final String pulsarToken;
    private final String pulsarTopic;

    public MpMessageService(@Value("${pulsar-service-url}") String pulsarUrl, @Value("${pulsar-auth-token}") String pulsarToken, @Value("${pulsar-producer-topic}") String pulsarTopic) {
        this.pulsarUrl = pulsarUrl;
        this.pulsarToken = pulsarToken;
        this.pulsarTopic = pulsarTopic;
    }

    public void saveMessageTo(Xml message) throws IOException {
        var keyFileContent = System.getenv("PULSAR_KEY_FILE");
        var keyFile = PulsarKeyFile.fromJson(keyFileContent);

        System.out.println("Key file content: ");
        System.out.println("Type: " + keyFile.type);
        System.out.println("Client ID: " + keyFile.client_id);
        System.out.println("Client Secret" + keyFile.client_secret);
        System.out.println("Issuer URL: " + keyFile.issuer_url);
        System.out.println("topic: " + pulsarTopic);
        System.out.println("serviceUrl: " + pulsarUrl);

        try {
            var authenticationOAuth2 = new AuthenticationOAuth2();
            var objectMapper = new ObjectMapper();
            var params = Maps.newHashMap();
            var data = Maps.newHashMap();
            data.put("client_id", keyFile.client_id);
            data.put("client_secret: ", keyFile.client_secret);

            params.put("privateKey", "data:application/json;base64," + new String(Base64.getEncoder().encode(objectMapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8))));
            params.put("issuerUrl", keyFile.issuer_url);
            params.put("audience", "urn:sn:pulsar:uniheart:alpha");

            authenticationOAuth2.configure(objectMapper.writeValueAsString(params));

            var client = PulsarClient.builder().serviceUrl(pulsarUrl).authentication(
                    authenticationOAuth2
            ).build();

            String json = new Gson().toJson(message);
            System.out.println("Connected to pulsar! will send message: " + json);

            var producer = client.newProducer().topic(pulsarTopic).producerName("uniheart-producer").create();

            producer.send(json.getBytes());
            producer.close();
            client.close();
        } catch (PulsarClientException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to pulsar or failed to create producer!");
            throw e;
        }
    }

    public synchronized Xml getMessageFor(String ticket) throws PulsarClientException {
        var client = PulsarClient.builder().serviceUrl(pulsarUrl).authentication(AuthenticationFactory.token(pulsarToken)).build();
        var consumer = client.newConsumer().topic(pulsarTopic).subscriptionName("my-subscription").subscribe();
        var xml = new Xml().fromUserName("empty");

        var received = false;

        var count = 0;

        do {
            var msg = consumer.receive(1, TimeUnit.SECONDS);
            count++;
            if (msg != null) {
                var json = new String(msg.getData());

                try {
                    xml = new Gson().fromJson(json, Xml.class);

                    received = xml.getTicket().equals(ticket);

                    if (received) {
                        consumer.acknowledge(msg);
                    }
                } catch (Exception ex) {
                    logger.error("Failed to parse json: " + json);
                    xml.fromUserName(json);

                    consumer.acknowledge(msg);
                }
            }
        } while (!received && count < 30);

        consumer.close();
        client.close();

        return xml;
    }
}
