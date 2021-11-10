package com.my.payment.command;

import com.my.payment.constants.Message;
import com.my.payment.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("errorMessage", Message.NO_SUCH_COMMAND);
        return Path.ERROR_PAGE;
    }
}
