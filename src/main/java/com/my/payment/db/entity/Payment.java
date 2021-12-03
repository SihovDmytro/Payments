package com.my.payment.db.entity;

import com.my.payment.db.PaymentStatus;
import com.my.payment.util.DateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Payment bean
 */

@XmlRootElement(name = "product")
public class Payment implements Serializable {
    int id;
    Card from;
    Card to;
    double amount;
    Calendar date;
    PaymentStatus status;

    public Payment() {
    }

    public Payment(Card from, Card to, Calendar date, double amount, PaymentStatus status) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public Payment(int id, Card from, Card to, Calendar date, double amount, PaymentStatus status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    @XmlElement(name = "amount")
    public double getAmount() {
        return amount;
    }

    @XmlElement(name = "from")
    public Card getFrom() {
        return from;
    }

    @XmlElement(name = "to")
    public Card getTo() {
        return to;
    }


    public Calendar getDate() {
        return date;
    }

    @XmlElement(name = "status")
    public PaymentStatus getStatus() {
        return status;
    }

    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(Card from) {
        this.from = from;
    }

    public void setTo(Card to) {
        this.to = to;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTextDate() {
        return date.get(Calendar.YEAR) + "-" + String.format("%2s", date.get(Calendar.MONTH) + 1).replace(' ', '0') + "-" + String.format("%2s", date.get(Calendar.DAY_OF_MONTH)).replace(' ', '0');
    }

    public String getTextDateTime() {
        String datetime = getTextDate() + " " + String.format("%2s", date.get(Calendar.HOUR_OF_DAY)).replace(' ', '0') + ":" + String.format("%2s", date.get(Calendar.MINUTE)).replace(' ', '0') + ":" + String.format("%2s", date.get(Calendar.SECOND)).replace(' ', '0');
        return datetime;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", date=" + getTextDateTime() +
                ", status=" + status +
                '}';
    }
}
