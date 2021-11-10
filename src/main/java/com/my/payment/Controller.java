package com.my.payment;

import com.my.payment.command.Command;
import com.my.payment.command.CommandContainer;
import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(value = "/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String commandName = request.getParameter("command");
        logger.trace("Parameter command ==>"+commandName);
        Command command = CommandContainer.get(commandName);
        logger.trace("Obtained command ==>"+commandName);
        String forward = Path.ERROR_PAGE;
        try {
            forward = command.execute(request, response);
        } catch (Exception ex) {
            logger.warn("Execute exception ==>"+ex);
            request.setAttribute("errorMessage", ex.getMessage());
        }
        logger.trace("Forward ==>"+forward);
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
