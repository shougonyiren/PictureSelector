package com.liuaho.base;

import android.app.Application;

import androidx.room.Room;

import com.liuaho.repository.AppDatabase;

public class MyApp extends Application {

    private static  MyApp mInstance;
    private AppDatabase appDB;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appDB = Room.databaseBuilder(this,AppDatabase.class,"dynamic_info")
                .addMigrations()
                .allowMainThreadQueries()
                .build();
    }

    public static MyApp getInstance(){
        return mInstance;
    }

    public AppDatabase getAppDB(){
        return appDB;
    }

}
