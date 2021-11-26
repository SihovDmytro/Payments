package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChangeLocaleCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("ChangeLocaleCommand starts");
        String lang = request.getParameter("language");
        LOG.trace("lang parameter ==> "+lang);
        request.getServletContext().setAttribute("language",lang);
        HttpSession session = request.getSession();
        Locale locale = new Locale(lang);
        ResourceBundle rb = ResourceBundle.getBundle("localization",locale);
        request.getServletContext().setAttribute("resBundle",rb);
        LOG.trace("resBundle ==> "+rb);
        session.setAttribute("resultTitle", "Success");
        session.setAttribute("resultMessage", "Language is changed");
        return Path.RESULT_PAGE;
    }
}
