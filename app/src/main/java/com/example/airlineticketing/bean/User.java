package com.example.airlineticketing.bean;

import java.io.Serializable;

/**
 * 用户
 */
public class User implements Serializable {
    private Integer id;
    private String account;//账号
    private String password;//密码
    private String nickName;//昵称
    private String number;//身份证
    private String email;//邮箱
    private Integer userType;//用户类型
    private Double balance;//余额

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User(Integer id, String account, String password, String nickName, String number, String email, Integer userType, Double balance) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.number = number;
        this.email = email;
        this.userType = userType;
        this.balance = balance;
    }
}
