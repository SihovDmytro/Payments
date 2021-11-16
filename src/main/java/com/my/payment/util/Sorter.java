package com.my.payment.util;

import com.my.payment.db.entity.Payment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorter {
    public static final Comparator<Payment> SORT_BY_DATE = (o1, o2) -> o1.getDate().compareTo(o2.getDate());

    public static void sortPaymentsByDate(List<Payment> payments)
    {
        payments.sort(SORT_BY_DATE.reversed());
    }
}
