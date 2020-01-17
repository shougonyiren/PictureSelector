package com.liuaho.repository;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态
 */
@Entity
public class Dynamic  implements Parcelable {
    @NonNull
    @PrimaryKey
    private  long time;//时间戳

    private  String conent;//内容
    @NonNull
    private  List<LocalMedia>  localMediaList=new ArrayList<LocalMedia>();//图视频集


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
        dest.writeLong(time);
        dest.writeString(conent);
        dest.writeArray(new List[]{localMediaList});
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    public void setLocalMediaList(List<LocalMedia> localMediaList) {
        this.localMediaList = localMediaList;
    }
}
