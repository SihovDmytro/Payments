package com.my.payment.command;

import com.my.payment.Controller;
import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command{
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("LoginCommand starts");
        HttpSession s = request.getSession();
        String forward=Path.ERROR_PAGE;
        String login =  request.getParameter("login");
        logger.trace("Request parameter login ==> "+login);
        String password = request.getParameter("pass");
        logger.trace("Request parameter pass ==> "+password);
        DBManager dbManager = DBManager.getInstance();
        if(!dbManager.try2Login(login,password))
        {
            logger.trace("Cannot login");
            s.setAttribute("wrongData", Message.INVALID_CREDENTIALS);
            logger.trace("forward ==> "+forward);
            forward= "/"+Path.LOGIN_PAGE;
            return forward;
        }
        logger.trace("Login success");
        User user = dbManager.findUser(login);
        logger.trace("Found in DB user ==> "+user);
        Role userRole = user.getRole();
        logger.trace("userRole ==> "+userRole);
        forward="/"+Path.USER_CABINET;
        s.setAttribute("currUser",user);
        s.setAttribute("userRole",userRole);
        return forward;
    }
}
