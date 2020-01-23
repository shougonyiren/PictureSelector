package com.liuaho.repository;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 动态
 */
@Entity
public class Dynamic  implements Parcelable {
    @NonNull
    @PrimaryKey
    private String time;//时间戳

    private  String conent;//内容

    @TypeConverters(LocalMediaTypeConverter.class)
    private  List<LocalMedia>  localMediaList=new ArrayList<LocalMedia>();//图视频集

    public Dynamic(String conent, List<LocalMedia> localMediaList) {
        this.conent = conent;
        this.localMediaList = localMediaList;
        this.time=new Date().toString();
    }

    public Dynamic() {
        this.time = time;
    }

    protected Dynamic(Parcel in) {

    }

    public static final Creator<Dynamic> CREATOR = new Creator<Dynamic>() {
        @Override
        public Dynamic createFromParcel(Parcel in) {
            return new Dynamic(in);
        }

        @Override
        public Dynamic[] newArray(int size) {
            return new Dynamic[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(time.toString());
        dest.writeString(conent);
        dest.writeArray(new List[]{localMediaList});
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }

    public List<LocalMedia> getLocalMediaList() {
        return localMediaList;
    }
    public ArrayList<String> getpaths(){
        ArrayList<String> paths=new ArrayList<>();
        for (LocalMedia a:
            localMediaList ) {
            paths.add(a.getPath());
        }
        return paths;
    }
    public void setLocalMediaList(List<LocalMedia> localMediaList) {
        this.localMediaList = localMediaList;
    }
}
