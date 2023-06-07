package com.example.pictureplace;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    public String token;

    public String getToken() {
        return token;
    }
}
