package com.tensquare.sms.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    @Value("${aliyun.sms.signName}")
    private String signName;

    public void sendSms(Map<String, String> message) {
        System.out.println("手机号:" + message.get("mobile"));
        System.out.println("验证码:" + message.get("code"));
    }
}
