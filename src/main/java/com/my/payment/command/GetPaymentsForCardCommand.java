package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import com.my.payment.util.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GetPaymentsForCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(GetPaymentsForCardCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = Path.ERROR_PAGE;
        HttpSession session = request.getSession();
        int cardID;
        try {
            cardID = Integer.parseInt(request.getParameter("cardItem"));
            LOG.trace("Parameter cardID ==>"+cardID);
        }catch (NumberFormatException exception)
        {
            LOG.trace(Message.CANNOT_OBTAIN_HISTORY);
            request.setAttribute("errorMessage", Message.CANNOT_OBTAIN_HISTORY);
            return forward;
        }
        if(!checkCardID(cardID,(User) session.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", "You haven't this card");
            return forward;
        }
        DBManager dbManager = DBManager.getInstance();
        List<Payment> payments = dbManager.getPayments(cardID);
        LOG.trace("Obtained payments ==> "+payments);
        Sorter.sortPaymentsByDate(payments);
        LOG.trace("Payments after sorting==> "+payments);
        request.setAttribute("payments",payments);
        Card card = dbManager.getCardByID(cardID);
        if(card!=null)
        {
            request.setAttribute("currCard",card);
            forward=Path.HISTORY;
        }
        else {
            request.setAttribute("errorMessage", Message.CANNOT_OBTAIN_HISTORY);
        }
        return forward;
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
