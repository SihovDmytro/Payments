package com.my.payment.servlet;

import com.my.payment.db.DBManager;
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
            req.setAttribute("emailVal","Invalid email");
            continueReg=false;
        }
        if(!checkLogin(login)) {
            req.setAttribute("loginVal","Invalid login");
            continueReg=false;
        }
        if(!checkPass(pass)) {
            req.setAttribute("passVal","Invalid password");
            continueReg=false;
        }
        if(!pass.equals(passR)) {
            req.setAttribute("repeatPVal","Passwords are different");
            continueReg=false;
        }
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.isUserExists(login))
        {
            req.setAttribute("loginExist","This login already exists");
            continueReg=false;
        }
        if(!continueReg)
        {
            req.getRequestDispatcher("registration.jsp").forward(req,resp);
        }
        User user;


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
