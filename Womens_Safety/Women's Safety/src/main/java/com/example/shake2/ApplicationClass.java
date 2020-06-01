package com.example.shake2;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "9946FC5F-0766-BDE7-FFED-4AA8A9EF1800";
    public static final String API_KEY = "03A85DDD-5CC0-8BD3-FFBD-A5D864437700";
    public static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );



    }
}
