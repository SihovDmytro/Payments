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

public class ChangeCardStatusCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ChangeCardStatusCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forward = Path.ERROR_PAGE;
        HttpSession s = request.getSession();
        int cardID;
        try {
            cardID = Integer.parseInt(request.getParameter("cardID"));
            LOG.trace("Parameter cardID ==>"+cardID);
        }catch (NumberFormatException exception)
        {
            LOG.trace(Message.CANNOT_CHANGE_STATUS);
            request.setAttribute("errorMessage", Message.CANNOT_CHANGE_STATUS);
            return forward;
        }
        if(!checkCardID(cardID,(User) s.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", "You haven't this card");
            return forward;
        }
        Status newStatus = null;
        try{
            newStatus = Status.valueOf(request.getParameter("newStatus"));
            LOG.trace("Status ==> "+newStatus.toString());
        }catch (NumberFormatException e){
            LOG.trace("Cannot parse newStatus");
            request.setAttribute("errorMessage", Message.CANNOT_CHANGE_STATUS);
            return forward;
        }
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.changeCardStatus(cardID,newStatus))
        {
            LOG.trace("Card status is changed");
            forward=Path.GET_CARDS_COMMAND;

        }else {
            request.setAttribute("errorMessage",Message.CANNOT_CHANGE_STATUS);
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
