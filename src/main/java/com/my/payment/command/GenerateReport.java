package com.my.payment.command;

import com.my.payment.constants.MailType;
import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.entity.Payment;
import com.my.payment.util.Serializor;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.sf.jasperreports.engine.*;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class GenerateReport implements Command{
    private static final Logger LOG = LogManager.getLogger(GenerateReport.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("GenerateReport starts");
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        HttpSession session = request.getSession();
        String forward = Path.ERROR_PAGE;
        int paymentID;
        if((MailType)request.getAttribute("mailType") == MailType.PAYMENT)
        {
            paymentID = (int)request.getAttribute("paymentID");
        }else{
            try {
                LOG.trace("paymentID parameter ==> " + request.getParameter("paymentID"));
                paymentID = Integer.parseInt(request.getParameter("paymentID"));
            } catch (NumberFormatException exception) {
                LOG.warn("Cannot parse paymentID");
                throw new ServletException();
            }
        }

        DBManager dbManager = DBManager.getInstance();
        Payment payment = dbManager.getPaymentByID(paymentID);
        LOG.trace("Formed payment ==> " + payment);
        if (payment == null) {
            LOG.warn("Cannot get payment from DB");
            session.setAttribute("ErrorMessage","Cannot generate report");
            return forward;
        }
        String pathData = System.getProperty("reportDataFile");
        try {
            Serializor.serializeToXml(pathData, payment);
            LOG.trace("Serialization complete");
        }catch (JAXBException e){
            LOG.trace("Cannot serialize to xml");
        }

        String jrxmlFile = System.getProperty("reportTemplate");
        InputStream input = new FileInputStream(new File(jrxmlFile));
        try {
            Map params = new HashMap();
            Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream(pathData));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
            String pathPdf= System.getProperty("receipt");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathPdf);
            forward=Path.RECEIPT_PAGE;
        } catch (JRException e) {
            LOG.trace("Cannot generate report");

        }
        return forward;
    }
}
