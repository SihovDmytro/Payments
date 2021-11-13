package com.my.payment.listeners;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String path = servletContext.getRealPath("/WEB-INF/Payments.log");
        System.setProperty("logFile", path);
        System.out.println("logFile ==> "+path);
    }
}
