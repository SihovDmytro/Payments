package com.my.payment.command;

import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.Status;
import com.my.payment.db.entity.User;
import com.my.payment.util.PasswordHash;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class RegistrationCommandTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    ServletContext context;
    ResourceBundle rb;
    MockedStatic<DBManager> dbManagerStatic;
    DBManager dbManager;

    @Test
    public void execute() throws ServletException, IOException, NoSuchAlgorithmException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        context = mock(ServletContext.class);
        rb = mock(ResourceBundle.class);
        dbManager = mock(DBManager.class);
        dbManagerStatic = mockStatic(DBManager.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);
        when(context.getAttribute("resBundle")).thenReturn(rb);
        when(request.getParameter("email")).thenReturn("test@mail.com");
        when(request.getParameter("login")).thenReturn("testLog");
        when(request.getParameter("pass-repeat")).thenReturn("testpass");
        when(request.getParameter("pass")).thenReturn("testpass");
        dbManagerStatic.when(DBManager::getInstance).thenReturn(dbManager);
        User user = new User("test", Role.USER, PasswordHash.hash("testpass"), "1234567899876543", Status.ACTIVE);
        when(dbManager.findUser(anyString())).thenReturn(null);
        when(dbManager.addUser(any())).thenReturn(true);

        new RegistrationCommand().execute(request, response);

        verify(request, times(1)).getSession();
        verify(request, times(1)).getServletContext();
        verify(context, times(1)).getAttribute(anyString());
        verify(request, times(4)).getParameter(anyString());
        dbManagerStatic.verify(DBManager::getInstance, times(1));
        verify(dbManager, times(1)).addUser(any());
        verify(dbManager, times(1)).findUser(anyString());
        verify(session, times(3)).setAttribute(anyString(), any());

        dbManagerStatic.close();
    }
}