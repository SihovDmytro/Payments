package com.my.payment.listeners;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextListenerTest {
    private ServletContextEvent sce;
    private ServletContext sc;
    @BeforeEach
    void setUp() {
        sce = mock(ServletContextEvent.class);
        sc = mock(ServletContext.class);
    }

    @Test
    void contextInitialized() {
        when(sce.getServletContext()).thenReturn(sc);
        when(sc.getRealPath(anyString())).thenReturn("path1").thenReturn("path2").thenReturn("path3").thenReturn("path4");

        new ContextListener().contextInitialized(sce);
        assertEquals("path1",System.getProperty("logFile"));
        assertEquals("path2",System.getProperty("reportDataFile"));
        assertEquals("path3",System.getProperty("reportTemplate"));
        assertEquals("path4",System.getProperty("receipt"));
        verify(sce,times(1)).getServletContext();
        verify(sc,times(4)).getRealPath(anyString());
    }
}