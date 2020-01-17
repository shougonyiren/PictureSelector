package com.liuaho.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Dynamic.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DynamicDao  dynamicDao();
}

