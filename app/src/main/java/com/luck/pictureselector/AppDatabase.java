package com.luck.pictureselector;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.liuaho.repository.Dynamic;
import com.luck.pictureselector.ViewModel.DynamicDao;
@Database(entities = {Dynamic.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();
    public abstract DynamicDao dynamicDao();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Dynamic")
                                .build();
            }
            return INSTANCE;
        }
    }
}
