package com.dans.service.messaging;

import com.dans.service.messaging.entities.Message;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${jsa.rabbitmq.exchange}")
    private String exchange;

    @Value("${jsa.rabbitmq.routingkey}")
    private String routingKey;

    public void produceMsg(Message msg) {
        amqpTemplate.convertAndSend(exchange, routingKey, msg);
        System.out.println("Send msg = " + msg);
    }
}