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

public class CommitPaymentCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(CommitPaymentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("CommitPaymentCommand starts");
        String forward = Path.ERROR_PAGE;
        int paymentID;
        try {
            LOG.trace("paymentID parameter ==> " + request.getParameter("paymentID"));
            paymentID = Integer.parseInt(request.getParameter("paymentID"));
        } catch (NumberFormatException exception) {
            LOG.warn("Cannot parse paymentID");
            request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
            return forward;
        }

        DBManager dbManager = DBManager.getInstance();
        Payment payment = dbManager.getPaymentByID(paymentID);
        LOG.trace("Formed payment ==> " + payment);
        if(payment==null)
        {
            LOG.warn("Cannot get payment from DB");
            request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
            return forward;
        }
        if(payment.getFrom().getBalance()<payment.getAmount())
        {
            LOG.warn(Message.HAVE_NO_MONEY);
            request.setAttribute("errorMessage", Message.HAVE_NO_MONEY);
            return forward;
        }
        HttpSession session = request.getSession();
        if(dbManager.commitPayment(payment))
        {
            LOG.trace("Transaction complete ");
            session.setAttribute("resultTitle", "Success");
            session.setAttribute("resultMessage", Message.TRANSACTION_SUCCESS);
            forward = Path.RESULT_PAGE;
        } else {
            LOG.warn("Payment error");
            request.setAttribute("errorMessage", Message.CANNOT_MAKE_PAYMENT);
        }
        return forward;
    }
}
