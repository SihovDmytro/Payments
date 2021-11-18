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
        int fromID;
        HttpSession session = request.getSession();
        String forward = Path.ERROR_PAGE;
        try {
            fromID = Integer.parseInt(request.getParameter("fromID"));
        }catch (NumberFormatException exception)
        {
            LOG.warn("Cannot parse card id");
            request.setAttribute("errorMessage","Cannot parse card id");
            return forward;
        }
        LOG.trace("fromID parameter ==> "+fromID);
        if(!checkCardID(fromID,(User) session.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", "You haven't this card");
            return forward;
        }

        DBManager dbManager = DBManager.getInstance();
        Card card = dbManager.getCardByID(fromID);
        LOG.trace("card by id ==> "+card);
        if(card.getStatus()== Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            request.setAttribute("errorMessage", Message.CARD_IS_BLOCKED);
            return forward;
        }
        request.setAttribute("fromCard",card);
        return Path.MAKE_PAYMENT_PAGE;
    }
    private boolean checkCardID(int id, User user) {
        DBManager dbManager = DBManager.getInstance();
        List<Card> cards = dbManager.getCardsForUser(user);
        for(Card c: cards)
        {
            if(c.getCardID()==id) return true;
        }
        return false;
    }
}
