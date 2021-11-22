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
import java.math.BigDecimal;
import java.util.List;

public class TopUpCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(TopUpCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("TopUpCommand start");
        HttpSession session = request.getSession();
        String forward = Path.ERROR_PAGE;
        Card card = (Card) session.getAttribute("currCard");
        LOG.trace("card parameter ==> "+card);
        DBManager dbManager = DBManager.getInstance();
        if(card.getStatus()== Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            request.setAttribute("errorMessage", Message.CARD_IS_BLOCKED);
            return forward;
        }
        double amount;
        try {
            amount = Double.parseDouble(request.getParameter("topUp"));
        }catch (NumberFormatException exception)
        {
            LOG.warn("Cannot parse amount");
            request.setAttribute("errorMessage","Cannot parse amount");
            return forward;
        }
        LOG.trace("Amount ==> "+amount);
        forward="/controller?command=getPayments&cardItem="+card.getCardID();
        if(!checkAmount(amount,card))
        {
            LOG.trace("Amount out of bounds");
            request.setAttribute("amountLimit","Maximum amount = "+ BigDecimal.valueOf(999999999D - card.getBalance()).toPlainString());
        }
        else {
            if(!dbManager.topUpCard(card,amount))
            {
                LOG.warn(Message.CANNOT_TOP_UP);
                request.setAttribute("errorMessage", Message.CANNOT_TOP_UP);
                return Path.ERROR_PAGE;
            }
            else {
                LOG.warn(Message.TOP_UP_SUCCESS);
                session.setAttribute("resultTitle","Success");
                session.setAttribute("resultMessage",Message.TOP_UP_SUCCESS);
                forward=Path.RESULT_PAGE;
            }
        }
        return forward;
    }
    private boolean checkAmount(double amount,Card card)
    {
        return !(card.getBalance() + amount > 999999999);
    }
}
