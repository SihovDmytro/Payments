package com.my.payment.command;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Get cards command
 * @author Sihov Dmytro
 */
public class GetCardsCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("GetCardsCommand starts");
        List<Card> cards=null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currUser");
        Role role = (Role) session.getAttribute("userRole");
        DBManager dbManager = DBManager.getInstance();
        if(role == Role.USER)
            cards = dbManager.getCardsForUser(user);
        else if(role == Role.ADMIN)
            cards = dbManager.getAllCards();

        request.setAttribute("listCards",cards);
        LOG.trace("Obtained cards ==> "+cards);
        List<Payment> payments = new ArrayList<>();
        for(Card card : cards)
        {
            if(card.getStatus()== Status.ACTIVE)
                payments.addAll(dbManager.getSentPayments(card));
        }

        LOG.trace("Payments ==> "+payments);
        request.setAttribute("allPayments",payments);
        return "/"+Path.CARDS_PAGE;
    }
}
