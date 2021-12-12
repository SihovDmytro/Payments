package com.my.payment.command;

import com.my.payment.constants.MailType;
import com.my.payment.constants.Path;
import com.my.payment.db.entity.User;
import com.my.payment.filters.AccessFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class SendEmailCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SendEmailCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("SendEmailCommand starts");
        String redirect = Path.REGISTRATION_PAGE;
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("newUser");
        LOG.trace("user ==> " + user);
        MailType type = (MailType) session.getAttribute("mailType");
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

        } catch (MessagingException ex) {
            LOG.trace("Cannot send email. "+ex.getMessage());
        }
        return redirect;
    }
}
