package com.example.pictureplace;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    public Context getAppContext(){
        Context mContext = getApplicationContext();
        return mContext;
    }
}
