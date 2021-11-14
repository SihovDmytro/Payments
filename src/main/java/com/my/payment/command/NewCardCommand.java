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
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(NewCardCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("NewCardCommand starts");
        String name = request.getParameter("name");
        LOG.trace("Parameter name ==> "+name);
        String cardNumber = request.getParameter("cardNumber");
        LOG.trace("Parameter cardNumber ==> "+cardNumber);
        String cvv = request.getParameter("cvv");
        LOG.trace("Parameter cvv ==> "+cvv);
        String expDate = request.getParameter("exp-date");
        LOG.trace("Parameter exp-date ==> "+expDate);
        boolean valid = true;
        if(!checkName(name))
        {
            LOG.trace(Message.INVALID_CARD_NAME);
            request.setAttribute("invalidName",Message.INVALID_CARD_NAME);
            valid=false;
        }
        if(!checkExpDate(expDate))
        {
            LOG.trace(Message.INVALID_EXPIRATION_DATE);
            request.setAttribute("invalidExpDate",Message.INVALID_EXPIRATION_DATE);
            valid=false;
        }
        if(!checkNumber(cardNumber))
        {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            request.setAttribute("invalidNumber",Message.INVALID_CARD_NUMBER);
            valid=false;
        }
        if(!checkCVV(cvv))
        {
            LOG.trace(Message.INVALID_CVV);
            request.setAttribute("invalidCVV",Message.INVALID_CVV);
            valid=false;
        }
        if(valid)
        {
            LOG.trace("Valid parameters");
            Calendar calendar =  Calendar.getInstance();
            calendar.setTime(Date.valueOf(expDate));
            Status cardStatus = Status.ACTIVE;
            if(calendar.compareTo( Calendar.getInstance())<0)
            {
                cardStatus = Status.BLOCKED;
                LOG.trace("Card is out of date.");
            }
            DBManager dbManager = DBManager.getInstance();
            Card card = new Card(name,cardNumber,calendar,Integer.parseInt(cvv),0, cardStatus);
            User user = (User) request.getSession().getAttribute("currUser");
            if(dbManager.findCard(card,user))
            {
                LOG.trace(Message.CARD_ALREADY_ADDED);
                request.setAttribute("alreadyAdded",Message.CARD_ALREADY_ADDED);
                return Path.NEW_CARD_PAGE;
            }
            else{
                if(dbManager.addNewCard(card,user))
                {
                    LOG.trace(Message.CARD_ADD_SUCCESS);
                    request.setAttribute("isSuccess",Message.CARD_ADD_SUCCESS);
                }
                else{
                    LOG.trace(Message.CANNOT_ADD_CARD);
                    request.setAttribute("isSuccess",Message.CANNOT_ADD_CARD);
                }
            }
        }
        else {
            LOG.trace("invalid parameters");
        }
        return Path.NEW_CARD_PAGE;
    }



    private boolean checkExpDate(String txt)
    {
        if(txt==null) return false;
        try{
            Calendar calendar =  Calendar.getInstance();
            calendar.setTime(Date.valueOf(txt));
            int year= calendar.get(Calendar.YEAR);
            int currYear= Calendar.getInstance().get(Calendar.YEAR);
            return Math.abs(year-currYear)<=4;
        }
        catch (IllegalArgumentException exception){
            return false;
        }
    }
    private boolean checkCVV(String txt)
    {
        if(txt==null) return false;
        Pattern p = Pattern.compile("^\\d{3}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length()==3;
    }
    private boolean checkNumber(String txt)
    {
        if(txt==null) return false;
        Pattern p = Pattern.compile("^\\d{16}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length()==16;
    }
    private boolean checkName(String txt)
    {
        if(txt==null) return true;
        return txt.length() <= 45;
    }
}
