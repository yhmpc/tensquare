package com.tensquare.sms.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    public void sendSms(Map<String, String> message) {
        System.out.println("手机号:" + message.get("mobile"));
        System.out.println("验证码:" + message.get("code"));
    }
}
