package com.my.payment.listeners;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Locale;
import java.util.ResourceBundle;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();
        String path = servletContext.getRealPath("/WEB-INF/Payments.log");
        System.setProperty("logFile", path);
        System.out.println("logFile ==> "+path);
        servletContext.setAttribute("language","en_US");
        Locale locale=new Locale("en","US");
        ResourceBundle rb = ResourceBundle.getBundle("localization",locale);
        System.out.println("resBundle ==> "+rb);
        servletContext.setAttribute("resBundle",rb);

        path=servletContext.getRealPath("/WEB-INF/payment.xml");
        System.setProperty("reportDataFile", path);
        System.out.println("Report data ==> "+path);

        path=servletContext.getRealPath("/WEB-INF/classes/report/report.jrxml");
        System.setProperty("reportTemplate", path);
        System.out.println("Report template ==> "+path);

        path=servletContext.getRealPath("receipt.pdf");
        System.setProperty("receipt", path);
        System.out.println("Report receipt ==> "+path);
    }
}
