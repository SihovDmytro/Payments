package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = request.getSession();
        String forward;
        System.out.println(s);
        String login =  request.getParameter("login");
        String password = request.getParameter("pass");
        request.setAttribute("login",login);
        request.setAttribute("pass", password);
        DBManager dbManager = DBManager.getInstance();
        if(!dbManager.try2Login(login,password))
        {
            request.setAttribute("wrongData", Message.INVALID_CREDENTIALS);
            forward= Path.LoginPage;
            return forward;
        }
        User user = dbManager.findUser(login);
        s.setAttribute("currUser",user);
        request.getRequestDispatcher("account.jsp").forward(request,response);
        return null;
    }
}
