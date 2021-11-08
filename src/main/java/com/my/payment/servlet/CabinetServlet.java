package com.my.payment.servlet;

import com.my.payment.constants.Message;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.User;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/cabinet")
public class CabinetServlet extends HttpServlet {
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession s = request.getSession();
        System.out.println(s);
        System.out.println(s.isNew());
        String login =  request.getParameter("login");
        String password = request.getParameter("pass");
        request.setAttribute("login",login);
        request.setAttribute("pass", password);
        DBManager dbManager = DBManager.getInstance();
        if(!dbManager.try2Login(login,password))
        {
            request.setAttribute("wrongData", Message.INVALID_CREDENTIALS);
            request.getRequestDispatcher("index.jsp").forward(request,response);
        }
        User user = dbManager.findUser(login);
        s.setAttribute("currUser",user);
        request.getRequestDispatcher("account.jsp").forward(request,response);
    }

    public void destroy() {
    }
}