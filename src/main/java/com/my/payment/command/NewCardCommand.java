package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * New card command. Add existing card to user
 * @author Sihov Dmytro
 */
public class NewCardCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(NewCardCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("NewCardCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        HttpSession session = request.getSession();
        String cardNumber = request.getParameter("cardNumber");
        LOG.trace("Parameter cardNumber ==> " + cardNumber);
        String cvv = request.getParameter("cvv");
        LOG.trace("Parameter cvv ==> " + cvv);
        String pin = request.getParameter("pin");
        LOG.trace("Parameter pin ==> " + pin);
        String expDate = request.getParameter("exp-date");
        LOG.trace("Parameter exp-date ==> " + expDate);
        boolean valid = true;
        if (!checkExpDate(expDate)) {
            LOG.trace(Message.INVALID_EXPIRATION_DATE);
            session.setAttribute("invalidExpDate", rb.getString("message.invalidExpDate"));
            valid = false;
        }
        if (!checkNumber(cardNumber)) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            session.setAttribute("invalidNumber", rb.getString("message.invalidCardNumber"));
            valid = false;
        }
        if (!checkCVV(cvv)) {
            LOG.trace(Message.INVALID_CVV);
            session.setAttribute("invalidCVV", rb.getString("message.invalidCVV"));
            valid = false;
        }
        if (!checkPIN(pin)) {
            LOG.trace("Invalid PIN");
            session.setAttribute("invalidPIN", rb.getString("message.invalidPIN"));
            valid = false;
        }
        if (valid) {
            LOG.trace("Valid parameters");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.valueOf(expDate));
            DBManager dbManager = DBManager.getInstance();
            Card card = dbManager.getCardByNumber(cardNumber);
            if (card == null) {
                LOG.trace("Card number doesn't exists");
                session.setAttribute("doesNotEx", rb.getString("message.invNumber"));
                return Path.NEW_CARD_PAGE;
            }
            User user = (User) session.getAttribute("currUser");
            if (dbManager.findCard(card)) {
                LOG.trace(Message.CARD_ALREADY_ADDED);
                session.setAttribute("alreadyAdded", rb.getString("message.cardIsNotAvailable"));
                return Path.NEW_CARD_PAGE;
            }
            if (card.getDate().equals(calendar) && card.getPin() == Integer.parseInt(pin) && card.getCvv() == Integer.parseInt(cvv)) {
                if (dbManager.addCard(card, user)) {
                    LOG.trace(Message.CARD_ADD_SUCCESS);
                    session.setAttribute("isSuccess", rb.getString("message.cardAdded"));
                } else {
                    LOG.trace(Message.CANNOT_ADD_CARD);
                    session.setAttribute("isSuccess", rb.getString("message.cannotAddCard"));
                }
            } else {
                LOG.trace("Wrong endDate/cvv/pin");
                session.setAttribute("wrongData", rb.getString("message.wrongData"));
            }
        } else {
            LOG.trace("invalid parameters");
        }
        return Path.NEW_CARD_PAGE;
    }

    private boolean checkExpDate(String txt) {
        if (txt == null) return false;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.valueOf(txt));
            int year = calendar.get(Calendar.YEAR);
            int currYear = Calendar.getInstance().get(Calendar.YEAR);
            return Math.abs(year - currYear) <= 5;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean checkCVV(String txt) {
        if (txt == null) return false;
        Pattern p = Pattern.compile("^\\d{3}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length() == 3;
    }

    private boolean checkNumber(String txt) {
        if (txt == null) return false;
        Pattern p = Pattern.compile("^\\d{16}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length() == 16;
    }

    private boolean checkPIN(String txt) {
        if (txt == null) return false;
        Pattern p = Pattern.compile("^\\d{4}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length() == 4;
    }
}
