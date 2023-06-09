package com.example.pictureplace;
import com.google.gson.annotations.SerializedName;

public class LoginDTO {
    @SerializedName("cookie")
    public String cookie;

    public static class Cookie{
        @SerializedName("access")
        public String access;

        @SerializedName("refresh")
        public String refresh;

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getRefresh() {
            return refresh;
        }

        public void setRefresh(String access) {
            this.refresh = refresh;
        }
    }
}
