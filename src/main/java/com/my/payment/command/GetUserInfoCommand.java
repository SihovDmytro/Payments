package com.my.payment.command;

import com.my.payment.constants.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetUserInfoCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(GetUserInfoCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("GetUserInfoCommand starts");
        return Path.USER_CABINET;
    }
}
