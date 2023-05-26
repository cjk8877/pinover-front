package com.example.pictureplace;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("userid")
    public String userId;
    @SerializedName("password")
    public String password;

    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LoginRequest(String inputId, String inputPw) {
        this.userId = inputId;
        this.password = inputPw;
    }


}
