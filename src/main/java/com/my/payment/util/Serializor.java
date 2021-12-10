package com.my.payment.util;

import com.my.payment.db.entity.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.*;

public class Serializor {
    private static final Logger LOG = LogManager.getLogger(Serializor.class);

    /**
     * Serializes payment class to file
     * @param path path to file
     * @param payment
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static void serializeToXml(String path, Payment payment) throws JAXBException, FileNotFoundException {
            JAXBContext context = JAXBContext.newInstance(payment.getClass());
            Marshaller marshaller = null;
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
            marshaller.marshal(payment,new FileOutputStream(path));
    }
}
