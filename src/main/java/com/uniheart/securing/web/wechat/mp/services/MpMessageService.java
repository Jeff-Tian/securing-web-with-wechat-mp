package com.uniheart.securing.web.wechat.mp.services;

import com.uniheart.wechatmpservice.models.Xml;
import org.springframework.beans.factory.annotation.Value;
import org.apache.pulsar.client.api.*;
import org.springframework.stereotype.Component;

@Component
public class MpMessageService {
    private final String pulsarUrl;
    private final String pulsarToken;
    private final String pulsarTopic;

    public MpMessageService(@Value("${pulsar-service-url}") String pulsarUrl, @Value("${pulsar-auth-token}") String pulsarToken, @Value("${pulsar-producer-topic}") String pulsarTopic) {
        this.pulsarUrl = pulsarUrl;
        this.pulsarToken = pulsarToken;
        this.pulsarTopic = pulsarTopic;
    }

    public void saveMessageTo(Xml message) throws PulsarClientException {
        var client = PulsarClient.builder().serviceUrl(pulsarUrl).authentication(AuthenticationFactory.token(pulsarToken)).build();
        var producer = client.newProducer().topic(pulsarTopic).create();
        producer.send(message.toString().getBytes());
        producer.close();
        client.close();
    }
}
