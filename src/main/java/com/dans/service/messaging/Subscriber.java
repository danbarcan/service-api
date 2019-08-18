package com.dans.service.messaging;

import com.dans.service.messaging.entities.Message;
import com.dans.service.services.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Subscriber {
    @Autowired
    MailService mailService;

    @RabbitListener(queues = "${jsa.rabbitmq.queue}")
    public void recievedMessage(Message msg) throws IOException, MessagingException {
        System.out.println("Recieved Message: " + msg);
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
                Logger.getLogger(this.getClass().getName()).log(Level.ALL, msg.getMessageType() + " not recognized");
        }
    }
}
