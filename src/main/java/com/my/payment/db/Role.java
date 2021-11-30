package com.my.payment.db;

import com.my.payment.db.entity.User;

public enum Role {
    USER(1), ADMIN(2);
    private final int id;
    Role(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name().toLowerCase();
    }

    public int getId() {
        return id;
    }
}
