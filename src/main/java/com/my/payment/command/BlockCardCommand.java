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

public class BlockCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(BlockCardCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = Path.ERROR_PAGE;
        HttpSession s = request.getSession();
        int cardID;
        try {
            cardID = Integer.parseInt(request.getParameter("cardItem"));
            LOG.trace("Parameter cardID ==>"+cardID);
        }catch (NumberFormatException exception)
        {
            LOG.trace(Message.CANNOT_BLOCK_CARD);
            request.setAttribute("errorMessage", Message.CANNOT_BLOCK_CARD);
            return forward;
        }
        if(!checkCardID(cardID,(User) s.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", "You haven't this card");
            return forward;
        }
        DBManager dbManager = DBManager.getInstance();
        Card card = dbManager.getCardByID(cardID);
        if(card.getStatus() == Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            forward="/controller?command=getCards";
            return forward;
        }
        if(dbManager.blockCard(card))
        {
            forward="/controller?command=getCards";
            LOG.trace("The card was blocked");
        }
        else {
            request.setAttribute("errorMessage",Message.CANNOT_BLOCK_CARD);
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
