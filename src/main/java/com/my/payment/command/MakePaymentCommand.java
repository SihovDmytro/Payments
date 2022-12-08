package com.my.payment.command;

import com.my.payment.constants.MailType;
import com.my.payment.constants.Message;
import com.my.payment.constants.NumericConstants;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.PaymentStatus;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.threads.SendEmailThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Make payment command
 *
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
        if (!validateNumber(numberTo)) {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            session.setAttribute("invalidCardToNumber", rb.getString("message.invNumber"));
            valid = false;
        }
        if (!validateAmount(amount, card)) {
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
                int newPaymentID = dbManager.makePayment(payment);
                LOG.trace("newPaymentID ==> " + newPaymentID);
                if (newPaymentID != 0) {

                    LOG.trace("Transaction complete ");
                    session.setAttribute("resultTitle", "Success");
                    session.setAttribute("resultMessage", rb.getString("message.transactionSuccess"));
                    request.setAttribute("mailType", MailType.PAYMENT);
                    request.setAttribute("paymentID", newPaymentID);
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

    private boolean validateAmount(double amount, Card card) {
        boolean check = true;
        if (amount < NumericConstants.MIN_AMOUNT || amount > NumericConstants.MAX_AMOUNT)
            check = false;

        return card.getBalance() >= amount && check;
    }

    private boolean validateNumber(String txt) {
        if (txt == null) return false;
        Pattern p = Pattern.compile("^\\d{16}$");
        Matcher m = p.matcher(txt);
        DBManager dbManager = DBManager.getInstance();
        return m.find() && txt.length() == NumericConstants.NUMBER_LENGTH && dbManager.getCardByNumber(txt) != null;
    }

}
