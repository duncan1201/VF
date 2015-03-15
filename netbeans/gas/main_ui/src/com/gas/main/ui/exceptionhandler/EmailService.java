/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.exceptionhandler;

import com.gas.common.ui.util.CipherUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IEmailService.class)
public class EmailService implements IEmailService {

    @Override
    public String getReportBugsEmail(){
        return "bugs@vectorfriends.com";
    }
    
    @Override
    public boolean sendEmail(String to, String subject, String body, File attachment) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("body cannot be null");
        }
        boolean success = true;
        Session mailSession = createSession();

        try {

            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject(subject);
            message.setFrom(new InternetAddress("sgvectorfriends@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setContent(body, "text/html");

            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(body);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            if (attachment != null) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attachment.getName());
            }
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            transport.connect();

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException e) {
            Exceptions.printStackTrace(e);
            success = false;
        } catch (MessagingException e) {
            Exceptions.printStackTrace(e);
            success = false;
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            success = false;
        } finally {
            return success;
        }
    }

    private Session createSession() {
        Session mailSession = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String pwd = null;
                try {
                    pwd = new String(CipherUtil.toBytes("6a696e677975616e"), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                }
                return new PasswordAuthentication("sgvectorfriends@gmail.com", pwd);
            }
        });
        return mailSession;
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.socketFactory.port", 25);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        return props;
    }

    @Override
    public boolean sendEmail(String to, String subject, String body) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        if (body == null) {
            throw new IllegalArgumentException("body cannot be null");
        }
        boolean success = true;

        Session mailSession = createSession();

        try {

            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject(subject);
            message.setFrom(new InternetAddress("sgvectorfriends@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setContent(body, "text/html");
            transport.connect();

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException e) {
            Exceptions.printStackTrace(e);
            success = false;
        } catch (MessagingException e) {
            Exceptions.printStackTrace(e);
            success = false;
        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            success = false;
        } finally {
            return success;
        }
    }
}
