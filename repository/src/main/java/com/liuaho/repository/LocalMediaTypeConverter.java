package com.liuaho.repository;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.entity.LocalMedia;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class LocalMediaTypeConverter {

    Gson gson = new Gson();

    @TypeConverter
    public List<LocalMedia> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<LocalMedia>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String someObjectListToString(List<LocalMedia> someObjects) {
        return gson.toJson(someObjects);
    }

}
