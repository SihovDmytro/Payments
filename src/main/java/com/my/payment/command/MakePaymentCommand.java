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
import java.util.List;

public class MakePaymentCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(MakePaymentCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String forward = Path.ERROR_PAGE;
        Card card = (Card)session.getAttribute("currCard");
        LOG.trace("card parameter ==> "+card);
        DBManager dbManager = DBManager.getInstance();
        if(card.getStatus()== Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            request.setAttribute("errorMessage", Message.CARD_IS_BLOCKED);
            return forward;
        }
        request.setAttribute("fromCard",card);
        return Path.MAKE_PAYMENT_PAGE;
    }

}
