package com.my.payment.command;


import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Cancel payment command
 */
public class CancelPaymentCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(CancelPaymentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("CancelPaymentCommand starts");
        HttpSession session = request.getSession();
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        String forward = Path.ERROR_PAGE;
        int paymentID;
        try {
            LOG.trace("paymentID parameter ==> " + request.getParameter("paymentID"));
            paymentID = Integer.parseInt(request.getParameter("paymentID"));
        } catch (Exception exception) {
            LOG.warn("Cannot parse paymentID");

            session.setAttribute("ErrorMessage", rb.getString("message.cannotCancelPay"));
            return forward;
        }

        DBManager dbManager = DBManager.getInstance();
        Payment payment = dbManager.getPaymentByID(paymentID);
        LOG.trace("Formed payment ==> " + payment);
        if (payment == null) {
            LOG.warn("Cannot get payment from DB");
            session.setAttribute("ErrorMessage", rb.getString("message.cannotCancelPay"));
            return forward;
        }
        if (dbManager.cancelPayment(payment)) {
            LOG.trace("Payment is canceled");
            session.setAttribute("resultTitle", rb.getString("message.success"));
            session.setAttribute("resultMessage", rb.getString("message.cancelSuccess"));
            forward = Path.RESULT_PAGE;
        } else {
            LOG.warn("Payment error");
            session.setAttribute("ErrorMessage", rb.getString("message.cannotCancelPay"));
        }
        return forward;
    }
}
