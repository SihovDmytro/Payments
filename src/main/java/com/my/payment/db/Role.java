package com.my.payment.db;

import com.my.payment.db.entity.User;

public enum Role {
    USER(1), ADMIN(2);
    private final int id;
    private Role(int id)
    {
        this.id = id;
    }
//    public static Role getRole(User user) {
//        int roleID = user.getRoleID();
//        return Role.values()[roleID-1];
//    }
    public String getName()
    {
        return name().toLowerCase();
    }

    public int getId() {
        return id;
    }
}
