package com.my.payment.util;

import com.my.payment.db.entity.Card;
import com.my.payment.db.entity.Payment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sorter class
 * @author Sihov Dmytro
 */
public class Sorter {
    private static final Comparator<Payment> SORT_PAYMENTS_BY_DATE = Comparator.comparing(Payment::getDate);
    public static final Comparator<Card> SORT_CARDS_BY_DATE = Comparator.comparing(Card::getDate);
    public static void sortPaymentsByDate(List<Payment> payments,boolean rev)
    {
        if (rev)
            payments.sort(SORT_PAYMENTS_BY_DATE.reversed());
        else
            payments.sort(SORT_PAYMENTS_BY_DATE);
    }
    public static void sortCardsByDate(List<Card> cards,boolean rev)
    {
        if(rev)
             cards.sort(SORT_CARDS_BY_DATE.reversed());
        else
            cards.sort(SORT_CARDS_BY_DATE);
    }
}
