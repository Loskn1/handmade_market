package com.example.handmademarket.dto;

public class LoginRequest {

    private String useraccount;
    private String password;

    public String getUserAccount() {
        return useraccount;
    }

    public void setUserAccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
