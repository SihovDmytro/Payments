package com.my.payment.db.entity;

import com.my.payment.db.Status;

import javax.jws.soap.SOAPBinding;

public class User {
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
        this.login = login;
        this.roleID = roleID;
        this.password = password;
        this.email = email;
        this.status = status;
    }
    public User() {}
}
