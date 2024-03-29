package com.my.payment.command;

import com.my.payment.constants.MailType;
import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.User;
import com.my.payment.threads.SendEmailThread;
import com.my.payment.util.PasswordHash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registration command
 *
 * @author Sihov Dmytro
 */
public class RegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegistrationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("RegistrationCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        logger.trace("resBundle ==> " + rb);
        boolean continueReg = true;
        String email = request.getParameter("email");
        logger.trace("Request parameter email ==> " + email);
        String login = request.getParameter("login");
        logger.trace("Request parameter login ==> " + login);
        String pass = request.getParameter("pass");
        logger.trace("Request parameter pass ==> " + pass);
        String passR = request.getParameter("pass-repeat");
        logger.trace("Request parameter pass-repeat ==> " + passR);
        String fullName = request.getParameter("fullName");
        logger.trace("Request parameter fullName ==> " + fullName);
        String birth = request.getParameter("birth");
        logger.trace("Request parameter birth ==> " + birth);
        String forward = Path.REGISTRATION_PAGE;
        HttpSession session = request.getSession();
        if (!checkEmail(email)) {
            logger.warn(Message.INVALID_EMAIL);
            session.setAttribute("emailVal", rb.getString("message.invalidEmail"));
            continueReg = false;
        }
        if (!checkLogin(login)) {
            logger.warn(Message.INVALID_LOGIN);
            session.setAttribute("loginVal", rb.getString("message.invalidLogin"));
            continueReg = false;
        }
        if (!checkPass(pass)) {
            logger.warn(Message.INVALID_PASS);
            session.setAttribute("passVal", rb.getString("message.invalidPass"));
            continueReg = false;
        }
        if (!checkPass(passR) || !pass.equals(passR)) {
            logger.warn(Message.DIFFERENT_PASS);
            session.setAttribute("repeatPVal", rb.getString("message.difPass"));
            continueReg = false;
        }
        if (!checkDate(birth)) {
            logger.trace(Message.INVALID_EXPIRATION_DATE);
            session.setAttribute("invalidDate", "Неправильний фомат дати");
            continueReg = false;
        }
        DBManager dbManager = DBManager.getInstance();
        if (dbManager.findUser(login) != null) {
            logger.warn(Message.LOGIN_EXISTS);
            session.setAttribute("loginExist", rb.getString("message.loginEx"));
            continueReg = false;
        }
        try {
            pass = PasswordHash.hash(pass);
        } catch (NoSuchAlgorithmException exception) {
            logger.warn("Cannot hash password");
            session.setAttribute("isSuccess", rb.getString("message.cannotCreateUser"));
            continueReg = false;
        }
        if (continueReg) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.valueOf(birth));
            User user = new User(login, Role.USER, pass, email, Status.ACTIVE, fullName, calendar);
            logger.warn("Formed user ==> " + user);
            if (dbManager.addUser(user)) {
                logger.warn(Message.USER_CREATED);
                session.setAttribute("isSuccess", rb.getString("message.userCreate"));
                request.setAttribute("currUser", user);
                request.setAttribute("mailType", MailType.REGISTRATION);
                forward = Path.REGISTRATION_PAGE;

            } else {
                session.setAttribute("isSuccess", rb.getString("message.cannotCreateUser"));
            }
        } else {
            logger.warn(Message.CANNOT_CREATE_USER);
        }
        return forward;
    }

    private boolean checkEmail(String text) {
        if (text == null) return false;
        Pattern p = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 45;
    }

    private boolean checkLogin(String text) {
        if (text == null) return false;
        Pattern p = Pattern.compile("^[\\w_-]{3,20}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 20;
    }

    private boolean checkPass(String text) {
        if (text == null) return false;
        return text.length() <= 45 && text.length() > 5;
    }

    private boolean checkDate(String txt) {
        if (txt == null) return false;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.valueOf(txt));
            int year = calendar.get(Calendar.YEAR);
            int currYear = Calendar.getInstance().get(Calendar.YEAR);
            return currYear >= year && currYear - year < 150;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean checkFullName(String fullName) {
        return fullName != null && fullName.length() < 80;
    }
}
