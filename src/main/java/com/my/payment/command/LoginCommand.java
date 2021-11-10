package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession s = request.getSession();
        String forward;
        System.out.println(s);
        String login =  request.getParameter("login");
        String password = request.getParameter("pass");
        System.out.println("login="+login+"\npassword="+password);
        DBManager dbManager = DBManager.getInstance();
        if(!dbManager.try2Login(login,password))
        {
            System.out.println("Error cred");
            request.setAttribute("wrongData", Message.INVALID_CREDENTIALS);
            forward= Path.LOGIN_PAGE;
            return forward;
        }
        User user = dbManager.findUser(login);
        Role userRole = Role.getRole(user);
        forward=Path.USER_CABINET;
        System.out.println("user="+user);
        s.setAttribute("currUser",user);
        s.setAttribute("userRole",userRole);
        return forward;
    }
}
