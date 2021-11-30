package com.my.payment.command;

import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Error command
 * @author Sihov Dmytro
 */
public class ErrorCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ErrorCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("ErrorCommand starts");
        HttpSession session = request.getSession();
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        session.setAttribute("ErrorMessage", rb.getString("message.noCommand"));
        return Path.ERROR_PAGE;
    }
}
