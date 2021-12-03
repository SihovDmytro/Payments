package com.my.payment.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAdapter extends XmlAdapter<String, Calendar> {
    private DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

    @Override
    public Calendar unmarshal(String v) throws Exception {
        Calendar c =Calendar.getInstance();
        c.setTime(dateFormat.parse(v));
        return c;
    }

    @Override
    public String marshal(Calendar v) throws Exception {
        return dateFormat.format(v.getTime());
    }
}
