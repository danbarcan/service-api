package com.dans.service.messaging;

import com.dans.service.messaging.entities.Message;
import com.dans.service.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Subscriber {
    private static final Logger log = LoggerFactory.getLogger(Subscriber.class);

    @Autowired
    MailService mailService;

    @RabbitListener(queues = "${jsa.rabbitmq.queue}")
    public void recievedMessage(Message msg) {
        log.info("Recieved Message: {}", msg);
        switch (msg.getMessageType()) {
            case NEW_JOB:
                mailService.sendNewJobMail(msg);
                break;
            case REGISTER:
                mailService.sendRegisterMail(msg);
                break;
            case NEW_OFFER:
                mailService.sendNewOfferMail(msg);
                break;
            case ACCEPTED_OFFER:
                mailService.sendAcceptedOfferMail(msg);
                break;
            default:
                log.info("Message type: {} not recognized", msg.getMessageType());
        }
    }
}
