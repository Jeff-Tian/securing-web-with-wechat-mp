package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpMessageService;
import com.uniheart.wechatmpservice.api.MpMessageApi;
import com.uniheart.wechatmpservice.models.Xml;
import io.swagger.annotations.ApiParam;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
public class WechatMessageController implements MpMessageApi {
    Logger logger = LoggerFactory.getLogger(WechatMessageController.class);

    private final MpMessageService mpMessageService;

    public WechatMessageController(MpMessageService mpMessageService) {
        this.mpMessageService = mpMessageService;
    }

    @Override
    public ResponseEntity<Void> mpMessage(@ApiParam(value = "wechat mp messages in xml format", required = true) @Valid @RequestBody Xml xml) {
        try {
            this.mpMessageService.saveMessageTo(xml);
            logger.info("saved info: " + xml);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PulsarClientException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
