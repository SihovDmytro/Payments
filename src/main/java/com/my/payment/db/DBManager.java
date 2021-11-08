package com.my.payment.db;

import com.my.payment.db.entity.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class DBManager {
    private static final String FIND_USER="SELECT * FROM user WHERE login=?";
    private static final String ADD_USER="INSERT INTO user (login, roles_id, pass, email, status) VALUES (?,?,?,?,?)";
    private static final String TRY_TO_LOGIN="SELECT * FROM user WHERE login=? AND pass=?";
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
    private void close(Connection con, Statement statement, ResultSet rs) {
        close(rs);
        close(con);
        close(statement);
    }
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
    private void close(Connection con) {
        if (con != null ) {
            try {
                con.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
    public User findUser(String login)
    {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try{
            con = dbManager.getConnection();
            ps = con.prepareStatement(FIND_USER);
            ps.setString(1,login);
            rs = ps.executeQuery();
            if(rs.next()) {
                return new User(rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), Status.valueOf(rs.getString(6).toUpperCase()));
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        finally {
            close(con,ps,rs);
        }
        return null;
    }
    public boolean addUser(User user)
    {
        PreparedStatement ps = null;
        Connection con = null;
        try{
            con = dbManager.getConnection();
            ps = con.prepareStatement(ADD_USER);
            ps.setString(1,user.getLogin());
            ps.setInt(2,user.getRoleID());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getEmail());
            ps.setString(5,user.getStatus().toString());
        int count = ps.executeUpdate();
        if(count>0) return true;
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        finally {
            close(con);
            close(ps);
        }
        return false;
    }

    public boolean try2Login(String login, String password)
    {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try{
            con = dbManager.getConnection();
            ps = con.prepareStatement(TRY_TO_LOGIN);
            ps.setString(1,login);
            ps.setString(2,password);
            rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        finally {
            close(con,ps,rs);
        }
        return false;
    }
}
