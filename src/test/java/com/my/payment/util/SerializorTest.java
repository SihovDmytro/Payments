package com.my.payment.util;

import com.my.payment.db.PaymentStatus;
import com.my.payment.db.Status;
import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SerializorTest {
    @Test
    public void serializeToXml() throws JAXBException, FileNotFoundException {
        MockedStatic<JAXBContext> jaxbMock = mockStatic(JAXBContext.class);
        MockedStatic<Serializor> ser = mockStatic(Serializor.class);
        JAXBContext context = mock(JAXBContext.class);
        Marshaller marshaller = mock(Marshaller.class);

        ser.when(() -> Serializor.serializeToXml(anyString(), any())).thenCallRealMethod();
        jaxbMock.when(() -> JAXBContext.newInstance((Class) any())).thenReturn(context);
        when(context.createMarshaller()).thenReturn(marshaller);
        doNothing().when(marshaller).marshal(any(), (File) any());

        Card card1 = new Card("название", "1999567899876543", Calendar.getInstance(), 1, 1000, Status.ACTIVE, 123);
        Card card2 = new Card("название2", "1111222233334444", Calendar.getInstance(), 1234, 123, Status.ACTIVE, 321);
        Payment payment = new Payment(card1, card2, Calendar.getInstance(), 100.50, PaymentStatus.PREPARED);
        Serializor.serializeToXml("/WEB-INF/payment.xml", payment);

        jaxbMock.verify(() -> JAXBContext.newInstance((Class) any()), times(1));
        ser.verify(() -> Serializor.serializeToXml(anyString(), any()));
        verify(context, times(1)).createMarshaller();
        verify(marshaller, times(2)).setProperty(anyString(), any());
        ser.close();
        jaxbMock.close();
    }
}