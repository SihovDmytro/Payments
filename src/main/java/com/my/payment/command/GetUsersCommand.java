package com.my.payment.command;

import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Get all users command
 * @author Sihov Dmytro
 */
public class GetUsersCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(GetUsersCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.trace("GetUsersCommand starts");
        DBManager dbManager = DBManager.getInstance();
        List<User> users = dbManager.getAllUsers();
        LOG.trace("Users ==> " + users);
        request.setAttribute("users", users);
        return Path.USERS_PAGE;
    }
}
