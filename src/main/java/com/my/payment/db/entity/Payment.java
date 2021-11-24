package com.my.payment.db.entity;

import com.my.payment.db.PaymentStatus;

import java.math.BigDecimal;
import java.util.Calendar;

public class Payment {
    int id;
    Card from;
    Card to;
    double amount;
    Calendar date;
    PaymentStatus status;

    public Payment(Card from, Card to,Calendar date, double amount, PaymentStatus status) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }
    public Payment(int id, Card from, Card to,Calendar date, double amount, PaymentStatus status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Card getFrom() {
        return from;
    }

    public void setFrom(Card from) {
        this.from = from;
    }

    public Card getTo() {
        return to;
    }

    public void setTo(Card to) {
        this.to = to;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    public String getTextDate(){
        return date.get(Calendar.YEAR)+"-"+String.format("%2s",date.get(Calendar.MONTH)+1).replace(' ', '0')+"-"+String.format("%2s",date.get(Calendar.DAY_OF_MONTH)).replace(' ', '0');    }
    public String getTextDateTime(){
        String datetime = getTextDate()+" "+String.format("%2s",date.get(Calendar.HOUR_OF_DAY)).replace(' ', '0')+":"+String.format("%2s",date.get(Calendar.MINUTE)).replace(' ', '0')+":"+String.format("%2s",date.get(Calendar.SECOND)).replace(' ', '0');
        return datetime;
    }
    public String textAmount()
    {
        return new BigDecimal(amount).toPlainString();
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
