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
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(CreateCardCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("CreateCardCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> "+rb);
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        LOG.trace("Parameter name ==> "+name);
        String cardNumber = request.getParameter("cardNumber");
        LOG.trace("Parameter cardNumber ==> "+cardNumber);
        String cvv = request.getParameter("cvv");
        LOG.trace("Parameter cvv ==> "+cvv);
        String pin = request.getParameter("pin");
        LOG.trace("Parameter pin ==> "+pin);
        boolean valid = true;
        if(!checkName(name))
        {
            LOG.trace(Message.INVALID_CARD_NAME);
            session.setAttribute("invalidName",rb.getString("message.invalidCardName"));
            valid=false;
        }
        if(!checkNumber(cardNumber))
        {
            LOG.trace(Message.INVALID_CARD_NUMBER);
            session.setAttribute("invalidNumber",rb.getString("message.invalidCardNumber"));
            valid=false;
        }
        if(!checkCVV(cvv))
        {
            LOG.trace(Message.INVALID_CVV);
            session.setAttribute("invalidCVV",rb.getString("message.invalidCVV"));
            valid=false;
        }
        if(!checkPIN(pin))
        {
            LOG.trace("Invalid PIN");
            session.setAttribute("invalidPIN",rb.getString("message.invalidPIN"));
            valid=false;
        }
        if(valid)
        {
            LOG.trace("Valid parameters");
            DBManager dbManager = DBManager.getInstance();
            Card card = new Card(name,cardNumber,Calendar.getInstance(),Integer.parseInt(cvv),0, Status.ACTIVE,Integer.parseInt(pin));
            LOG.trace("Formed card ==> "+ card);
            User user = (User) session.getAttribute("currUser");
            if (dbManager.getCardByNumber(card.getNumber()) != null) {
                LOG.trace("This number already exists");
                session.setAttribute("alreadyExist", rb.getString("message.numberExists"));
                return Path.CREATE_CARD_PAGE;
            }
            if(dbManager.createNewCard(card,user))
            {
                LOG.trace(Message.CARD_ADD_SUCCESS);
                session.setAttribute("isSuccess",rb.getString("message.cardAdded"));
            }
            else{
                LOG.trace(Message.CANNOT_ADD_CARD);
                session.setAttribute("isSuccess",rb.getString("message.cannotAddCard"));
            }
        }
        else {
            LOG.trace("invalid parameters");
        }
        return Path.CREATE_CARD_PAGE;
    }
    private boolean checkCVV(String txt)
    {
        if(txt==null) return false;
        Pattern p = Pattern.compile("^\\d{3}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length()==3;
    }
    private boolean checkPIN(String txt)
    {
        if(txt==null) return false;
        Pattern p = Pattern.compile("^\\d{4}$");
        Matcher m = p.matcher(txt);
        return m.find() && txt.length()==4;
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
