package com.uniheart.securing.web.wechat.mp;

import com.uniheart.wechatmpservice.api.MpMessageApi;
import com.uniheart.wechatmpservice.models.Xml;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class WechatMessageController implements MpMessageApi {
    @Override
    public ResponseEntity<Void> mpMessage(@ApiParam(value = "wechat mp messages in xml format", required = true) @Valid @RequestBody Xml xml) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
