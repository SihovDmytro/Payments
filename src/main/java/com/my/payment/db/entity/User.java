package com.my.payment.db.entity;

import com.my.payment.db.Status;

import javax.jws.soap.SOAPBinding;

public class User {
    private int userID;
    private String login;
    private int roleID;
    private String password;
    private String email;
    private Status status;
    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", roleID=" + roleID +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User(String login, int roleID, String password, String email, Status status) {
        userID=-1;
        this.login = login;
        this.roleID = roleID;
        this.password = password;
        this.email = email;
        this.status = status;
    }
    public User(int userID,String login, int roleID, String password, String email, Status status) {
        this.userID=userID;
        this.login = login;
        this.roleID = roleID;
        this.password = password;
        this.email = email;
        this.status = status;
    }
    public User() {}
}
