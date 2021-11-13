package com.my.payment.command;

import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command{
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.debug("LogoutCommand starts");
        HttpSession s = request.getSession(false);
        logger.trace("Current session ==> "+s);
        if (s != null) {
            s.invalidate();
        }
        logger.debug("Session has been invalidated");
        return Path.LOGIN_PAGE;
    }
}
