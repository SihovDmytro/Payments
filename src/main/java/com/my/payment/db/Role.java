package com.my.payment.db;

import com.my.payment.db.entity.User;

import java.util.Locale;

public enum Role {
    USER, ADMIN;

    public static Role getRole(User user) {
        int roleID = user.getRoleID();
        return Role.values()[roleID];
    }
    public String getName()
    {
        return name().toLowerCase();
    }

}
