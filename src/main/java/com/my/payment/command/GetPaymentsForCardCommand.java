package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class GetPaymentsForCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(GetPaymentsForCardCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("GetPaymentsForCardCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> "+rb);
        String forward = Path.ERROR_PAGE;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currUser");
        int cardID;
        try {
            cardID = Integer.parseInt(request.getParameter("cardItem"));
        }catch (NumberFormatException exception)
        {
            LOG.trace(Message.CANNOT_OBTAIN_CARD_INFO);
            request.setAttribute("errorMessage", rb.getString("message.cannotObtainCardInfo"));
            return forward;
        }
        LOG.trace("Parameter cardID ==>"+cardID);
        if(user.getRole()== Role.USER && !checkCardID(cardID,(User) session.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", rb.getString("message.haveNoCard"));
            return forward;
        }
        DBManager dbManager = DBManager.getInstance();
        Card card = dbManager.getCardByID(cardID);
        if(card==null)
        {
            request.setAttribute("errorMessage", rb.getString("message.cannotObtainCardInfo"));
            return forward;
        }
        if(card.getStatus()== Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            request.setAttribute("errorMessage", rb.getString("message.cardBlocked"));
            return forward;
        }
        session.setAttribute("currCard",card);

        List<Payment> payments = dbManager.getPayments(card);
        request.setAttribute("payments",payments);
        LOG.trace("Payments ==> "+payments);
        forward="/"+Path.CARD_INFO;
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
