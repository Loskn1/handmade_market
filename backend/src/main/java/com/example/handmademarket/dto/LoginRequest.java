package com.example.handmademarket.dto;

public class LoginRequest {
    private String userAccount;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String userAccount, String password) {
        this.userAccount = userAccount;
        this.password = password;
    }

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
