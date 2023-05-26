package com.example.pictureplace;

import com.google.gson.annotations.SerializedName;

public class SignInRequest {
    @SerializedName("userid")
    public String userid;
    @SerializedName("password")
    public String password;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    @SerializedName("hp")
    public String hp;


    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getHp() {
        return hp;
    }
}
