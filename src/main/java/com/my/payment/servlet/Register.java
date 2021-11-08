package com.my.payment.servlet;

import com.my.payment.constants.Message;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.Status;
import com.my.payment.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/register")
public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        boolean continueReg = true;
        String email=req.getParameter("email");
        String login=req.getParameter("login");
        String pass=req.getParameter("pass");
        String passR=req.getParameter("pass-repeat");
        if(!checkEmail(email)) {
            req.setAttribute("emailVal",Message.INVALID_EMAIL);
            continueReg=false;
        }
        if(!checkLogin(login)) {
            req.setAttribute("loginVal",Message.INVALID_LOGIN);
            continueReg=false;
        }
        if(!checkPass(pass)) {
            req.setAttribute("passVal",Message.INVALID_PASS);
            continueReg=false;
        }
        if(!pass.equals(passR)) {
            req.setAttribute("repeatPVal",Message.DIFFERENT_PASS);
            continueReg=false;
        }
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.findUser(login)!=null)
        {
            req.setAttribute("loginExist",Message.LOGIN_EXISTS);
            continueReg=false;
        }
        if(continueReg) {
            User user = new User(login, Role.USER.getId(), pass, email, Status.ACTIVE);
            if (dbManager.addUser(user)) {
                req.setAttribute("isSuccess",Message.USER_CREATED);
            } else {
                req.setAttribute("isSuccess", Message.CANNOT_CREATE_USER);
            }
        }
        req.getRequestDispatcher("registration.jsp").forward(req,resp);

    }
    private boolean checkEmail(String text)
    {
        Pattern p = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 45;
    }
    private boolean checkLogin(String text)
    {
        Pattern p = Pattern.compile("^[\\w_-]{3,20}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 20;
    }
    private boolean checkPass(String text)
    {
        return text.length() <= 45 && text.length() >5;
    }
}
