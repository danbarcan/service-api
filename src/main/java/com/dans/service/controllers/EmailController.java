package com.dans.service.controllers;

import com.dans.service.messaging.Publisher;
import com.dans.service.messaging.entities.Message;
import com.dans.service.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class EmailController {

    @Autowired
    MailService mailService;

    @Autowired
    Publisher publisher;

    @GetMapping("/sendMail")
    public String sendMail() throws IOException, MessagingException {
        //mailService.sendmail("dan.barcan1994@gmail.com", "*****", new String[]{"fortza_madrid@yahoo.com"}, "Test Serviceul meu", "<h1>Test</h1>\n<b>DADADA</b>");
        //mailService.sendmail("Bine ai venit!","Bine ai venit la <b>Serviceul meu</b>", "danpopoutanu@gmail.com ", "welcome@serviceulmeu.ro", "Serviceul meu");

        //mailService.sendmail("Bine ai venit!","Bine ai venit la <b>Serviceul meu</b>", "dan.barcan1994@gmail.com ", "welcome@serviceulmeu.ro", "Serviceul meu");

        Message msg = new Message();
        msg.setEmailAddress("dan.barcan");
        msg.setName("dan");
        msg.setUsername("danb");
        publisher.produceMsg(msg);
        return "Mail sent";
    }
}
