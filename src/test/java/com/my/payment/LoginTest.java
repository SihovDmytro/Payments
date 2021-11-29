package com.my.payment;

import com.my.payment.db.DBManager;
import com.my.payment.db.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

import java.sql.*;

public class LoginTest {
    private static final String FIND_USER = "SELECT u.id, u.login, r.name, u.pass, u.email, u.status FROM user u join roles r on u.roles_id=r.id WHERE login=?";

    @Test
    public void tryToLoginSuccess() throws SQLException {

        System.out.println("DBManagerTest2#tryToLoginSuccess");
        DBManager dbManager = DBManager.getInstance();
        User user = dbManager.findUser("dmytro");
        System.out.println(user);
    }
}
