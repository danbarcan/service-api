package com.dans.service.messaging;

import com.dans.service.messaging.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    private static final Logger log = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${jsa.rabbitmq.exchange}")
    private String exchange;

    @Value("${jsa.rabbitmq.routingkey}")
    private String routingKey;

    public void produceMsg(Message msg) {
        amqpTemplate.convertAndSend(exchange, routingKey, msg);
        log.info("Send msg = {}", msg);
    }
}
