package com.luck.pictureselector;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.liuaho.repository.Dynamic;
import com.luck.pictureselector.ViewModel.DynamicDao;

@Database(entities = {Dynamic.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DynamicDao dynamicDao();
}

