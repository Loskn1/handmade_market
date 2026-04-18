package com.example.handmademarket.dto;

public class LoginRequest {

    private String userAccount;   // 输入：手机号 或 userAccount / adminAccount
    private String password;
    private Integer type;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
