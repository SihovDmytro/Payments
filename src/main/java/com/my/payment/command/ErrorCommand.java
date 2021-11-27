package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class ErrorCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ErrorCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("ErrorCommand starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> "+rb);
        request.setAttribute("errorMessage", rb.getString("message.noCommand"));
        return Path.ERROR_PAGE;
    }
}
