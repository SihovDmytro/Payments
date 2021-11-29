package com.my.payment;

import com.my.payment.command.Command;
import com.my.payment.command.CommandContainer;
import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(value = "/controller" , name = "Controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processPost(req,resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String commandName = request.getParameter("command");

        logger.trace("Parameter command ==>"+commandName);
        Command command = CommandContainer.get(commandName);
        HttpSession session = request.getSession();
        logger.trace("Obtained command ==>"+commandName);
        String forward = Path.ERROR_PAGE;
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        logger.trace("resBundle ==> "+rb);
        try {
            forward = command.execute(request, response);
        } catch (Exception ex) {
            logger.warn("Execute exception ==>"+ex);
            session.setAttribute("ErrorMessage", rb.getString("message.unknownErr"));
        }
        if (forward.equals("resultPage.jsp"))
        {
            response.sendRedirect(forward);
        }else {
            logger.trace("Forward ==> " + forward);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forward);
            dispatcher.forward(request, response);
        }
    }
    private void processPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String commandName = request.getParameter("command");
        HttpSession session = request.getSession();
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        logger.trace("resBundle ==> "+rb);
        logger.trace("Parameter command ==>"+commandName);
        Command command = CommandContainer.get(commandName);
        logger.trace("Obtained command ==>"+commandName);
        String redirect = Path.ERROR_PAGE;
        try {
            redirect = command.execute(request, response);
        } catch (Exception ex) {
            logger.warn("Execute exception ==>"+ex);
            session.setAttribute("ErrorMessage", rb.getString("message.unknownErr"));
        }
        logger.trace("Redirect ==> " + redirect);
        response.sendRedirect(redirect);
    }
}
