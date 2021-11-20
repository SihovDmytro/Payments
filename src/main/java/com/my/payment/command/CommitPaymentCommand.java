package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.PaymentStatus;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommitPaymentCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(CommitPaymentCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String numberFrom= request.getParameter("cardNumberFrom");
        LOG.trace("numberFrom parameter ==> "+numberFrom);
        String numberTo= request.getParameter("cardNumberTo");
        LOG.trace("numberTo parameter ==> "+numberTo);
        String forward = Path.ERROR_PAGE;
        double amount;
        try{
            LOG.trace("amount parameter ==> "+request.getParameter("amount"));
            amount = Double.parseDouble(request.getParameter("amount"));
        }catch (NumberFormatException exception)
        {
            LOG.warn("Cannot parse amount");
            request.setAttribute("errorMessage",Message.INVALID_AMOUNT);
            return forward;
        }
        if(!checkNumber(numberFrom))
        {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            request.setAttribute("errorMessage",Message.INVALID_CARD_NUMBER);
            return forward;
        }
        boolean valid=true;
        if(!checkNumber(numberTo))
        {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            request.setAttribute("invalidCardToNumber",Message.INVALID_CARD_NUMBER);
            valid=false;
        }
        if(!checkAmount(amount,numberFrom))
        {
            LOG.trace(Message.INVALID_AMOUNT);
            request.setAttribute("invalidAmount",Message.INVALID_AMOUNT);
            valid=false;
        }
        if(numberFrom.equals(numberTo))
        {
            LOG.trace("Enter another card");
            request.setAttribute("anotherCard","Enter another card");
            valid=false;
        }
        if(valid)
        {
            DBManager dbManager = DBManager.getInstance();
            LOG.trace("Valid parameters");
            if(numberFrom.equals(numberTo))
            {
                LOG.trace("Enter another card");
                request.setAttribute("anotherCard","Enter another card");

            }
            Payment payment = new Payment(dbManager.getCardByNumber(numberFrom),dbManager.getCardByNumber(numberTo), Calendar.getInstance(),amount, PaymentStatus.PREPARED);
            LOG.trace("Formed payment ==> "+payment);
            if(dbManager.commitPayment(payment)) {
                HttpSession s = request.getSession();
                s.setAttribute("resultTitle", "Success");
                s.setAttribute("resultMessage", Message.TRANSACTION_SUCCESS);
                forward=Path.RESULT_PAGE;
            }
            else {
                LOG.warn("Payment error");
                request.setAttribute("errorMessage","Payment error");
            }
        }else {
            LOG.trace("Invalid parameters");
            forward=Path.MAKE_PAYMENT_PAGE;
        }
        return forward;
    }

    private boolean checkAmount(double amount,String number) {
        boolean check = !(amount < 1) && !(amount > 999999999);
        DBManager dbManager = DBManager.getInstance();
        Card card = dbManager.getCardByNumber(number);
        return card.getBalance()>=amount;
    }

    private boolean checkNumber(String txt)
    {
        if(txt==null) return false;
        Pattern p = Pattern.compile("^\\d{16}$");
        Matcher m = p.matcher(txt);
        DBManager dbManager = DBManager.getInstance();
        return m.find() && txt.length()==16 && dbManager.getCardByNumber(txt)!=null;
    }
}
