package com.uniheart.securing.web.wechat.mp.services;

import com.google.gson.Gson;
import com.uniheart.wechatmpservice.models.Xml;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        var pathToKeyFile = Paths.get("keyfile.json");

        try {
            Files.write(pathToKeyFile, keyFileContent.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Key file content: ");
        System.out.println(new String(Files.readAllBytes((pathToKeyFile))));

        try {
            var client = PulsarClient.builder().serviceUrl(pulsarUrl).authentication(
                    AuthenticationFactoryOAuth2.clientCredentials(new URL("https://auth.streamnative.cloud/"), new URL(String.format("file://%s", pathToKeyFile)), "urn:sn:pulsar:uniheart:alpha")
            ).build();


            var producer = client.newProducer().topic(pulsarTopic).create();
            producer.send(new Gson().toJson(message).getBytes());
            producer.close();
            client.close();
        } catch (PulsarClientException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to pulsar!");
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
