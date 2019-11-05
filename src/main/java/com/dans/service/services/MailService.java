package com.dans.service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.port}")
    private String smtpPort;

    @Value("${smtp.username}")
    private String smtpUsername;

    @Value("${smtp.password}")
    private String smtpPassword;

    @Value("${mail.register}")
    private String registerMessage;

    @Value("${mail.job.new.user}")
    private String newJobUserMessage;

    @Value("${mail.job.new.service}")
    private String newJobServiceMessage;

    @Value("${mail.offer.new}")
    private String newOfferMessage;

    @Value("${mail.offer.accepted}")
    private String acceptedOfferMessage;

    @Value("${mail.offer.refused}")
    private String refusedOfferMessage;

    private String fromAddress = "no-reply@serviceulmeu.ro";

    private String fromName = "Serviceul meu";

    public void sendRegisterMail(com.dans.service.messaging.entities.Message message) {
        sendmail("Inregistrare reusita", registerMessage, message.getEmailAddress(), fromAddress, fromName);
    }

    public void sendNewJobMail(com.dans.service.messaging.entities.Message message) {
        sendmail("Cerere noua creata", newJobUserMessage, message.getJob().getUser().getEmail(), fromAddress, fromName);
    }

    public void sendNewOfferMail(com.dans.service.messaging.entities.Message message) {
        sendmail("Ai primit o noua oferta", newOfferMessage, message.getJob().getUser().getEmail(), fromAddress, fromName);
    }

    public void sendAcceptedOfferMail(com.dans.service.messaging.entities.Message message) {
        sendmail("Oferta ta a fost acceptata", acceptedOfferMessage, message.getJob().getAcceptedService().getEmail(), fromAddress, fromName);
    }

    public boolean sendmail(String title, String content, String toAddress, String fromAddress, String fromName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.checkserveridentity", true);
        try {

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromAddress, fromName));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            msg.setSubject(title);
            msg.setContent(content, "text/html");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (MessagingException me) {
            log.error("Error while sending mail.", me);
            return false;
        } catch (UnsupportedEncodingException e) {
            log.error("Error while sending mail.", e);
            return false;
        }
        return true;
    }
}
