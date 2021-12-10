package com.my.payment;

import com.my.payment.command.Command;
import com.my.payment.command.CommandContainer;
import com.my.payment.command.LoginCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<CommandContainer> container;
    private ServletContext context;
    private Command command;
    private ResourceBundle rb;
    private RequestDispatcher rd;
    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        rb = mock(ResourceBundle.class);
        command = mock(Command.class);
        context = mock(ServletContext.class);
        rd = mock(RequestDispatcher.class);
    }

    @Test
    public void doGet() throws ServletException, IOException {
        container = mockStatic(CommandContainer.class);
        when(request.getParameter("command")).thenReturn("login");
        container.when(() -> CommandContainer.get(anyString())).thenReturn(command);
        when(command.execute(request,response)).thenReturn("redirect");
        when(request.getServletContext()).thenReturn(context);
        when(context.getAttribute(anyString())).thenReturn(rb);
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        new Controller().doGet(request,response);

        verify(request,times(1)).getParameter(anyString());
        verify(request,times(1)).getServletContext();
        verify(context,times(1)).getAttribute(anyString());
        container.verify(() -> CommandContainer.get(anyString()),times(1));
        verify(command,times(1)).execute(request,response);
        verify(request,times(1)).getRequestDispatcher(anyString());
        verify(rd,times(1)).forward(request,response);
        container.close();
    }

    @Test
    public void doPost() throws ServletException, IOException {

        container = mockStatic(CommandContainer.class);
        when(request.getParameter("command")).thenReturn("login");
        container.when(() -> CommandContainer.get(anyString())).thenReturn(command);
        when(command.execute(request,response)).thenReturn("redirect");
        when(request.getServletContext()).thenReturn(context);
        when(context.getAttribute(anyString())).thenReturn(rb);

        new Controller().doPost(request,response);

        verify(request,times(1)).getParameter(anyString());
        verify(request,times(1)).getServletContext();
        verify(context,times(1)).getAttribute(anyString());
        container.verify(() -> CommandContainer.get(anyString()),times(1));
        verify(command,times(1)).execute(request,response);
        verify(response,times(1)).sendRedirect(anyString());

        container.close();
    }

}