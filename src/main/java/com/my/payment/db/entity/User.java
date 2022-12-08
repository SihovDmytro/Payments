package com.my.payment.db.entity;

import com.my.payment.db.Role;
import com.my.payment.db.Status;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

/**
 * User bean
 */
public class User implements Serializable {
    private int userID;
    private String login;
    private Role role;
    private String password;
    private String email;
    private Status status;
    private String fullName;
    private Calendar birth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID && Objects.equals(login, user.login) && role == user.role && Objects.equals(password, user.password) && Objects.equals(email, user.email) && status == user.status && Objects.equals(fullName, user.fullName) && Objects.equals(birth, user.birth);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", fullName='" + fullName + '\'' +
                ", birth=" + birth +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, login, role, password, email, status, fullName, birth);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Calendar getBirth() {
        return birth;
    }

    public void setBirth(Calendar birth) {
        this.birth = birth;
    }

    public int getUserID() {
        return userID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User(String login, Role role, String password, String email, Status status, String fullName, Calendar birth) {
        userID = -1;
        this.login = login;
        this.role = role;
        this.password = password;
        this.email = email;
        this.status = status;
        this.fullName = fullName;
        this.birth = birth;
    }

    public User(int userID, String login, Role role, String password, String email, Status status, String fullName, Calendar birth) {
        this.userID = userID;
        this.login = login;
        this.role = role;
        this.password = password;
        this.email = email;
        this.status = status;
        this.fullName = fullName;
        this.birth = birth;
    }

    public User() {
    }

    public String getTextDate() {
        return birth.get(Calendar.YEAR) + "-" + String.format("%2s", birth.get(Calendar.MONTH) + 1).replace(' ', '0') + "-" + String.format("%2s", birth.get(Calendar.DAY_OF_MONTH)).replace(' ', '0');
    }
}
