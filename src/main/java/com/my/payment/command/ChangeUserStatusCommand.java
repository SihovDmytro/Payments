package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeUserStatusCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ChangeUserStatusCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("ChangeUserStatusCommand starts");
        String forward = Path.ERROR_PAGE;
        int userID;
        try{
            userID = Integer.parseInt(request.getParameter("userID"));
            LOG.trace("Parameter userID ==>"+userID);
        }catch (NumberFormatException e){
            LOG.trace("Cannot parse userID");
            request.setAttribute("errorMessage", Message.CANNOT_CHANGE_STATUS);
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
        if(dbManager.changeUserStatus(userID,newStatus))
        {
            LOG.trace("User status is changed");
            forward=Path.GET_USERS_COMMAND;
        }else {
            request.setAttribute("errorMessage", Message.CANNOT_CHANGE_STATUS);
        }
        return forward;
    }
}
