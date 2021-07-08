package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpMessageService;
import com.uniheart.wechatmpservice.api.MpQrScanStatusApi;
import com.uniheart.wechatmpservice.models.MpQRScanStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public final class WechatMpQRScanStatusApiController implements MpQrScanStatusApi {
    private final MpMessageService mpMessageService;

    public WechatMpQRScanStatusApiController(MpMessageService mpMessageService) {
        this.mpMessageService = mpMessageService;
    }

    @Override
    public ResponseEntity<MpQRScanStatus> mpQrScanStatus(String ticket) {
        try {
            var xml = this.mpMessageService.getMessageFor(ticket);

            var user = new Object() {};

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("WechatMP"));


            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(new MpQRScanStatus().openId(xml.getFromUserName()).status("SCANNED"), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new MpQRScanStatus().openId(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
