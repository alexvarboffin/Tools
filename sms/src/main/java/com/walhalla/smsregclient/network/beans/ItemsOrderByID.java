package com.walhalla.smsregclient.network.beans;

/**
 * Created by combo on 24.03.2017.
 */

public class ItemsOrderByID {
    private String phone;

    private String email;

    private String histid;

    private String login;

    private String empas;

    private String pass;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHistid() {
        return histid;
    }

    public void setHistid(String histid) {
        this.histid = histid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmpas() {
        return empas;
    }

    public void setEmpas(String empas) {
        this.empas = empas;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "ClassPojo [phone = " + phone + ", email = " + email + ", histid = " + histid + ", login = " + login + ", empas = " + empas + ", pass = " + pass + "]";
    }
}