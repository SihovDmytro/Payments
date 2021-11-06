package com.my.payment.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/cabinet")
public class CabinetServlet extends HttpServlet {
    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession s = request.getSession();
        System.out.println(s);
        System.out.println(s.isNew());
        System.out.println(request.getParameter("login"));
        System.out.println(request.getParameter("pass"));
        request.setAttribute("login", request.getParameter("login"));
        request.setAttribute("pass", request.getParameter("pass"));
        request.getRequestDispatcher("account.jsp").forward(request,response);
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession s = request.getSession();
        System.out.println(s);
        System.out.println(s.isNew());
        System.out.println(request.getParameter("login"));
        System.out.println(request.getParameter("pass"));
        request.setAttribute("login", request.getParameter("login"));
        request.setAttribute("pass", request.getParameter("pass"));
        request.getRequestDispatcher("account.jsp").forward(request,response);
    }

    public void destroy() {
    }
}