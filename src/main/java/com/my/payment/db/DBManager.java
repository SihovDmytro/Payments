package com.my.payment.db;

import com.my.payment.db.entity.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {
    private static final String FIND_USER="SELECT * FROM user WHERE login=?";
    private static DBManager dbManager;
    private static String connectionUrl;
    private DataSource ds;
    private  DBManager()
    {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/MyDB");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection()
    {
        Connection con = null;
        try{
            con = ds.getConnection();
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
        return con;
    }
    public static synchronized DBManager getInstance()
    {
        if(dbManager==null)
        {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public boolean isUserExists(String login)
    {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try{
            con = dbManager.getConnection();
            ps = con.prepareStatement(FIND_USER);
            ps.setString(1,login);
            rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return true;
    }
    public void addUser(User user)
    {

    }
}
