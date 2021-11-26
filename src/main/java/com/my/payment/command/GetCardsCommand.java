package com.my.payment.command;

import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.User;
import com.my.payment.util.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetCardsCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("GetCardsCommand starts");
        List<Card> cards=null;
        HttpSession s = request.getSession();
        User user = (User) s.getAttribute("currUser");
        Role role = (Role) s.getAttribute("userRole");
        DBManager dbManager = DBManager.getInstance();
        if(role == Role.USER)
            cards = dbManager.getCardsForUser(user);
        else if(role == Role.ADMIN)
            cards = dbManager.getAllCards();

        request.setAttribute("listCards",cards);
        LOG.trace("Obtained cards ==> "+cards);
        return "/"+Path.CARDS_PAGE;
    }
}
