package com.example.handmademarket.dto;

public class RegisterRequest {
    private String userAccount;
    private String password;
    private String email;
    private String userName;
    private String phone;

    public RegisterRequest() {
    }

    public RegisterRequest(String userAccount, String password, String email, String userName, String phone) {
        this.userAccount = userAccount;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
