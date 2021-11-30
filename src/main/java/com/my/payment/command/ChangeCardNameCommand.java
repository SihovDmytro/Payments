package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * Change card name command
 * @author Sihov Dmytro
 */
public class ChangeCardNameCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ChangeCardNameCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("TopUpCommand start");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        HttpSession session = request.getSession();
        String forward = Path.ERROR_PAGE;
        Card card = (Card) session.getAttribute("currCard");
        LOG.trace("card parameter ==> " + card);
        DBManager dbManager = DBManager.getInstance();
        if (card == null || card.getStatus() == Status.BLOCKED) {
            LOG.trace(Message.CARD_IS_BLOCKED);
            session.setAttribute("ErrorMessage", rb.getString("message.cardBlocked"));
            return forward;
        }
        String newName = request.getParameter("cardName");
        LOG.trace("Name ==> " + newName);
        if (!checkName(newName)) {
            forward = Path.GET_CARD_INFO_COMMAND + "&cardItem=" + card.getCardID();
            LOG.trace("Invalid card name");
            session.setAttribute("invalidName", rb.getString("message.invalidCardName"));
        } else {
            if (!dbManager.changeCardName(card, newName)) {
                LOG.warn("Cannot change card name");
                session.setAttribute("ErrorMessage", rb.getString("message.cannotChangeCardName"));
                forward = Path.ERROR_PAGE;
            } else {
                LOG.warn(Message.TOP_UP_SUCCESS);
                forward = Path.GET_CARD_INFO_COMMAND + "&cardItem=" + card.getCardID();
            }
        }
        return forward;
    }
    private boolean checkName(String txt)
    {
        if(txt==null) return true;
        return txt.length() <= 45;
    }
}
