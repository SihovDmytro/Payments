package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.PaymentStatus;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakePaymentCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(MakePaymentCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("MakePaymentCommand starts");
        String numberTo = request.getParameter("cardNumberTo");
        LOG.trace("numberTo parameter ==> " + numberTo);
        String forward = Path.ERROR_PAGE;
        HttpSession session = request.getSession();
        Card card = (Card) session.getAttribute("currCard");
        LOG.trace("from card ==> "+card);
        if(card==null) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
            return forward;
        }
        double amount;
        try {
            LOG.trace("amount parameter ==> " + request.getParameter("amount"));
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException exception) {
            LOG.warn("Cannot parse amount");
            request.setAttribute("errorMessage", Message.INVALID_AMOUNT);
            return forward;
        }
        PaymentStatus ps = null;
        try {
            LOG.trace("PaymentStatus parameter ==> " + request.getParameter("prepareOrSend"));
            ps = PaymentStatus.valueOf(request.getParameter("prepareOrSend"));
        } catch (IllegalArgumentException exception) {
            LOG.warn("Cannot parse payment status");
            request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
            return forward;
        }
        boolean valid = true;
        if (!checkNumber(numberTo)) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            request.setAttribute("invalidCardToNumber", Message.INVALID_CARD_NUMBER);
            valid = false;
        }
        if (!checkAmount(amount, card)) {
            LOG.trace(Message.INVALID_AMOUNT);
            if(card.getBalance() >= amount)
                request.setAttribute("invalidAmount", Message.INVALID_AMOUNT);
            else request.setAttribute("invalidAmount", Message.HAVE_NO_MONEY);
            valid = false;
        }
        if (card.getNumber().equals(numberTo)) {
            LOG.trace("Enter another card");
            request.setAttribute("anotherCard", "Enter another card");
            valid = false;
        }
        if (valid) {
            DBManager dbManager = DBManager.getInstance();
            LOG.trace("Valid parameters");
            Payment payment = new Payment(card, dbManager.getCardByNumber(numberTo), Calendar.getInstance(), amount, ps);
            LOG.trace("Formed payment ==> " + payment);
            if(ps == PaymentStatus.SENT)
            {
                if (dbManager.makePayment(payment)) {

                    LOG.trace("Transaction complete ");
                    session.setAttribute("resultTitle", "Success");
                    session.setAttribute("resultMessage", Message.TRANSACTION_SUCCESS);
                    forward = Path.RESULT_PAGE;
                } else {
                    LOG.warn("Payment error");
                    request.setAttribute("errorMessage", "Payment error");
                }
            }
            if(ps == PaymentStatus.PREPARED)
            {
                if (dbManager.preparePayment(payment)) {
                    LOG.trace("Payment is prepared");
                    session.setAttribute("resultTitle", "Success");
                    session.setAttribute("resultMessage", Message.PAYMENT_PREPARED);
                    forward = Path.RESULT_PAGE;
                } else {
                    LOG.warn("Payment error");
                    request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
                }
            }
        } else {
            LOG.trace("Invalid parameters");
            forward = "/"+Path.MAKE_PAYMENT_PAGE;
        }
        return forward;
    }
    private boolean checkAmount(double amount, Card card) {
        boolean check = true;
        if(amount < 1 || amount > 999999999)
            check=false;

        return card.getBalance() >= amount && check;
    }

    private boolean checkNumber(String txt) {
        if (txt == null) return false;
        Pattern p = Pattern.compile("^\\d{16}$");
        Matcher m = p.matcher(txt);
        DBManager dbManager = DBManager.getInstance();
        return m.find() && txt.length() == 16 && dbManager.getCardByNumber(txt) != null;
    }

}
