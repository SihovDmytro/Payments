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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Make payment command
 * @author Sihov Dmytro
 */
public class MakePaymentCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(MakePaymentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("MakePaymentCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        String numberTo = request.getParameter("cardNumberTo");
        LOG.trace("numberTo parameter ==> " + numberTo);
        String forward = Path.ERROR_PAGE;
        HttpSession session = request.getSession();
        Card card = (Card) session.getAttribute("currCard");
        LOG.trace("from card ==> " + card);
        if (card == null) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            session.setAttribute("ErrorMessage", rb.getString("message.cannotMakePayment"));
            return forward;
        }
        double amount;
        try {
            LOG.trace("amount parameter ==> " + request.getParameter("amount"));
            amount = Double.parseDouble(request.getParameter("amount"));
        } catch (NumberFormatException exception) {
            LOG.warn("Cannot parse amount");
            session.setAttribute("ErrorMessage", rb.getString("message.invAmount"));
            return forward;
        }
        PaymentStatus ps = null;
        try {
            LOG.trace("PaymentStatus parameter ==> " + request.getParameter("prepareOrSend"));
            ps = PaymentStatus.valueOf(request.getParameter("prepareOrSend"));
        } catch (IllegalArgumentException exception) {
            LOG.warn("Cannot parse payment status");
            session.setAttribute("ErrorMessage", rb.getString("message.cannotMakePayment"));
            return forward;
        }
        boolean valid = true;
        if (!checkNumber(numberTo)) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            session.setAttribute("invalidCardToNumber", rb.getString("message.invNumber"));
            valid = false;
        }
        if (!checkAmount(amount, card)) {
            LOG.trace(Message.INVALID_AMOUNT);
            if (card.getBalance() >= amount)
                session.setAttribute("invalidAmount", rb.getString("message.invAmount"));
            else session.setAttribute("invalidAmount", rb.getString("message.haveNoMoney"));
            valid = false;
        }
        if (card.getNumber().equals(numberTo)) {
            LOG.trace("Enter another card");
            session.setAttribute("anotherCard", rb.getString("message.anotherCard"));
            valid = false;
        }
        if (valid) {
            DBManager dbManager = DBManager.getInstance();
            LOG.trace("Valid parameters");
            Payment payment = new Payment(card, dbManager.getCardByNumber(numberTo), Calendar.getInstance(), amount, ps);
            LOG.trace("Formed payment ==> " + payment);
            if (ps == PaymentStatus.SENT) {
                if (dbManager.makePayment(payment)) {

                    LOG.trace("Transaction complete ");
                    session.setAttribute("resultTitle", "Success");
                    session.setAttribute("resultMessage", Message.TRANSACTION_SUCCESS);
                    forward = Path.RESULT_PAGE;
                } else {
                    LOG.warn("Payment error");
                    session.setAttribute("ErrorMessage", rb.getString("message.cannotMakePayment"));
                }
            }
            if (ps == PaymentStatus.PREPARED) {
                if (dbManager.preparePayment(payment)) {
                    LOG.trace("Payment is prepared");
                    session.setAttribute("resultTitle", rb.getString("message.success"));
                    session.setAttribute("resultMessage", rb.getString("message.paymentPrep"));
                    forward = Path.RESULT_PAGE;
                } else {
                    LOG.warn("Payment error");
                    session.setAttribute("ErrorMessage", rb.getString("message.cannotMakePayment"));
                }
            }
        } else {
            LOG.trace("Invalid parameters");
            forward = Path.MAKE_PAYMENT_PAGE;
        }
        return forward;
    }

    private boolean checkAmount(double amount, Card card) {
        boolean check = true;
        if (amount < 1 || amount > 999999999)
            check = false;

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
