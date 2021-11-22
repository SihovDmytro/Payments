package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.NumericConst;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import com.my.payment.db.entity.User;
import com.my.payment.util.SortOrder;
import com.my.payment.util.SortType;
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
        }catch (NumberFormatException exception)
        {
            LOG.trace(Message.CANNOT_OBTAIN_CARD_INFO);
            request.setAttribute("errorMessage", Message.CANNOT_OBTAIN_CARD_INFO);
            return forward;
        }
        LOG.trace("Parameter cardID ==>"+cardID);
        if(!checkCardID(cardID,(User) session.getAttribute("currUser")))
        {
            LOG.trace("You haven't this card");
            request.setAttribute("errorMessage", "You haven't this card");
            return forward;
        }
        DBManager dbManager = DBManager.getInstance();
        Card card = dbManager.getCardByID(cardID);
        if(card==null)
        {
            request.setAttribute("errorMessage", Message.CANNOT_OBTAIN_CARD_INFO);
            return forward;
        }
        if(card.getStatus()== Status.BLOCKED)
        {
            LOG.trace(Message.CARD_IS_BLOCKED);
            request.setAttribute("errorMessage", Message.CARD_IS_BLOCKED);
            return forward;
        }
        session.setAttribute("currCard",card);

        int currentPage;
        int recordsPerPage;
        try {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }catch (NumberFormatException e)
        {
            LOG.trace("Cannot parse current page ");
            currentPage=1;
        }
        try {
            recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
        }catch (NumberFormatException e)
        {
            LOG.trace("Cannot parse recordsPerPage");
            recordsPerPage= NumericConst.defPageSize;
        }
        if(recordsPerPage<1) recordsPerPage=1;
        if(currentPage<1) currentPage=1;
        String sortType = request.getParameter("sort");
        LOG.trace("Sort type ==> "+sortType);
        String sortOrder = request.getParameter("sortOrder");
        LOG.trace("Sort order ==> "+sortOrder);
        SortType st = SortType.BY_DATE;
        SortOrder so = SortOrder.DESCENDING;
        try {
            st = SortType.valueOf(sortType);
        }catch (IllegalArgumentException | NullPointerException e){
            LOG.trace("Use default sort type");
        }
        request.setAttribute("sortType",st);
        try {
            so = SortOrder.valueOf(sortOrder);
        }catch (IllegalArgumentException | NullPointerException e){
            LOG.trace("Use default sort order");
        }
        LOG.trace("RecordsPerPage ==> "+recordsPerPage);
        LOG.trace("CurrentPage ==> "+currentPage);
        request.setAttribute("sortOrder",so);
        int offset = recordsPerPage*(currentPage-1);
        LOG.trace("offset ==> "+offset);
        List<Payment> paymentsOnPage = dbManager.getPayments(cardID,recordsPerPage,offset,st,so);
        int pageCount = (int)Math.ceil((double) dbManager.getPaymentsSize(cardID) / recordsPerPage);
        if(pageCount<1) pageCount=1;
        request.setAttribute("payments",paymentsOnPage);
        LOG.trace("Payments ==> "+paymentsOnPage);
        request.setAttribute("pageCount",pageCount);
        LOG.trace("PageCount ==> "+pageCount);
        request.setAttribute("currentPage",currentPage);

        request.setAttribute("recordsPerPage",recordsPerPage);


        forward=Path.CARD_INFO;
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
