package com.my.payment.db.entity;

import com.my.payment.db.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

/**
 *  Card bean
 */
public class Card implements Serializable {
    private int cardID;
    private String name;
    private String number;
    private Calendar date;
    private int cvv;
    private double balance;
    private Status status;
    private int pin;

    public Card() {
    }

    public Card(String name, String number, Calendar date, int cvv, double balance, Status status,int pin) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.cvv = cvv;
        this.balance = balance;
        this.status = status;
        this.pin=pin;
    }
    public Card(int id, String name, String number, Calendar date, int cvv, double balance, Status status, int pin) {
        cardID=id;
        this.name = name;
        this.number = number;
        this.date = date;
        this.cvv = cvv;
        this.balance = balance;
        this.status = status;
        this.pin=pin;
    }

    public int getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardID=" + cardID +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date=" + getTextDate() +
                ", cvv=" + cvv +
                ", balance=" + balance +
                ", status=" + status +
                ", pin=" + pin +
                '}';
    }

    public String getTextBalance()
    {
        return new BigDecimal(balance).toPlainString();
    }
    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getTextDate(){
        return date.get(Calendar.YEAR)+"-"+String.format("%2s",date.get(Calendar.MONTH)+1).replace(' ', '0')+"-"+String.format("%2s",date.get(Calendar.DAY_OF_MONTH)).replace(' ', '0');
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardID == card.cardID && cvv == card.cvv && Double.compare(card.balance, balance) == 0 && pin == card.pin && number.equals(card.number) && date.equals(card.date) && status == card.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardID, number, date, cvv, balance, status, pin);
    }
}
