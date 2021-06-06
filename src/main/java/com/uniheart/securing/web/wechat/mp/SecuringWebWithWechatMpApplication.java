package com.uniheart.securing.web.wechat.mp;

import com.uniheart.securing.web.wechat.mp.services.MpServiceBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecuringWebWithWechatMpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SecuringWebWithWechatMpApplication.class, args);

        MpServiceBean bean = context.getBean(MpServiceBean.class);

        System.out.println("-------------------------------------");
        System.out.println(bean.getQrCodeCreateUrl());
        context.close();

        SpringApplication.run(SecuringWebWithWechatMpApplication.class, args);
    }

}
