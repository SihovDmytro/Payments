package com.my.payment.threads;

import com.my.payment.command.GenerateReport;
import com.my.payment.command.LoginCommand;
import com.my.payment.command.SendEmailCommand;
import com.my.payment.constants.MailType;
import com.my.payment.constants.Path;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class SendEmailThread extends Thread{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private static final Logger LOG = LogManager.getLogger(SendEmailThread.class);
    public SendEmailThread(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
    }

    @Override
    public void run() {
        LOG.trace("SendEmail starts");


        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        HttpSession session = request.getSession();
        User user = null;
        MailType type = (MailType) request.getAttribute("mailType");
        LOG.trace("MailType ==> "+type);
        if(type==MailType.REGISTRATION)
            user = (User) request.getAttribute("currUser");
        else
            user= (User) session.getAttribute("currUser");
        LOG.trace("user ==> " + user);
        try (InputStream input = LoginCommand.class.getClassLoader().getResourceAsStream("mail.properties")) {
            Properties props = new Properties();
            props.load(SendEmailCommand.class.getClassLoader().getResourceAsStream("mail.properties"));

            Session mailSession = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("mail.from"), props.getProperty("mail.from.password"));
                }
            });

            LOG.trace("mailSession ==> " + mailSession);
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            LOG.trace("from ==> " + props.getProperty("mail.from"));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            if (type == MailType.REGISTRATION) {
                LOG.trace("Mail type ==> registration");
                message.setSubject(rb.getString("message.userCreate"));
                message.setText(rb.getString("email.registration.text").replace("{login}", user.getLogin()));
                LOG.trace("message ==> " + message);
                Transport.send(message);
            }
            else if(type == MailType.PAYMENT)
            {
                new GenerateReport().execute(request,response);
                message.setSubject(rb.getString("message.transactionSuccess"));
                MimeBodyPart bodyPart = new MimeBodyPart();
                String filename = System.getProperty("receipt");
                FileDataSource source = new FileDataSource(filename);
                bodyPart.setDataHandler(new DataHandler(source));
                bodyPart.setFileName(filename);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(bodyPart);
                message.setContent(multipart);
                Transport.send(message);
                LOG.trace("Email is sent");
            }

        } catch (MessagingException | IOException | ServletException ex) {
            LOG.trace("Cannot send email");
            ex.printStackTrace();
        }
    }
}
