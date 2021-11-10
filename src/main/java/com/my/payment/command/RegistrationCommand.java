package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.Status;
import com.my.payment.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean continueReg = true;
        String email=request.getParameter("email");
        String login=request.getParameter("login");
        String pass=request.getParameter("pass");
        String passR=request.getParameter("pass-repeat");
        String forward = Path.ERROR_PAGE;
        if(!checkEmail(email)) {
            request.setAttribute("emailVal", Message.INVALID_EMAIL);
            continueReg=false;
        }
        if(!checkLogin(login)) {
            request.setAttribute("loginVal",Message.INVALID_LOGIN);
            continueReg=false;
        }
        if(!checkPass(pass)) {
            request.setAttribute("passVal",Message.INVALID_PASS);
            continueReg=false;
        }
        if(!pass.equals(passR)) {
            request.setAttribute("repeatPVal",Message.DIFFERENT_PASS);
            continueReg=false;
        }
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.findUser(login)!=null)
        {
            request.setAttribute("loginExist",Message.LOGIN_EXISTS);
            continueReg=false;
        }
        if(continueReg) {
            User user = new User(login, Role.USER.getId(), pass, email, Status.ACTIVE);
            if (dbManager.addUser(user)) {
                request.setAttribute("isSuccess",Message.USER_CREATED);
            } else {
                request.setAttribute("isSuccess", Message.CANNOT_CREATE_USER);
            }
        }
        forward=Path.REGISTRATION_PAGE;
        return forward;
    }
    private boolean checkEmail(String text)
    {
        if(text==null) return false;
        Pattern p = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 45;
    }
    private boolean checkLogin(String text)
    {
        if(text==null) return false;
        Pattern p = Pattern.compile("^[\\w_-]{3,20}$");
        Matcher m = p.matcher(text);
        return m.find() && m.group().length() <= 20;
    }
    private boolean checkPass(String text)
    {
        if(text==null) return false;
        return text.length() <= 45 && text.length() >5;
    }
}
