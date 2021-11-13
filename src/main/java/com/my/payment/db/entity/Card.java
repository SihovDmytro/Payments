package com.my.payment.db.entity;

import com.my.payment.db.Status;

import java.util.Arrays;
import java.util.Calendar;

public class Card {
    private String name;
    private String number;
    private Calendar date;
    private int cvv;
    private double balance;
    private Status status;

    public Card() {
    }

    public Card(String name, String number, Calendar date, int cvv, double balance, Status status) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.cvv = cvv;
        this.balance = balance;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date=" + getTextDate() +
                ", cvv=" + cvv +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
    public String getTextDate(){
        return  String.format("%2s",date.get(Calendar.DAY_OF_MONTH)).replace(' ', '0')+"."+String.format("%2s",date.get(Calendar.MONTH)).replace(' ', '0')+"."+date.get(Calendar.YEAR);
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
}
