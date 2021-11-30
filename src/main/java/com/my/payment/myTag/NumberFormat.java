package com.my.payment.myTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Class contains functionality that fills spaces with zeros
 * @author Sihov Dmytro
 */
public class NumberFormat extends SimpleTagSupport{
    private int number;
    StringWriter sw = new StringWriter();

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void doTag() throws JspException, IOException {
        getJspBody().invoke(sw);
        String text = sw.toString();
        getJspContext().getOut().print(String.format("%"+number+"s",text).replace(' ', '0'));
    }
}
