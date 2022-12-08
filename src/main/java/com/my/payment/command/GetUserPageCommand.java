package com.my.payment.command;

import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetUserPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(GetUsersCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("login");
        LOG.trace("Request parameter login ==> " + login);
        DBManager dbManager = DBManager.getInstance();
        String forward = Path.ERROR_PAGE;
        User user = dbManager.findUser(login);
        LOG.trace("Found in DB user ==> " + user);
        if (user != null) {
            forward = "userPage.jsp";
            request.setAttribute("user", user);
        }
        return forward;
    }
}
