package com.luck.pictureselector.ViewModel;

import androidx.paging.DataSource;
import androidx.room.*;
import androidx.room.paging.LimitOffsetDataSource;


import com.liuaho.repository.Dynamic;

import java.util.List;

import io.reactivex.Completable;


@Dao
public interface DynamicDao {
    @Query("SELECT * FROM  Dynamic order by time desc")
    List<Dynamic> getAllAnime(); //加载所有动漫数据

    @Query("SELECT * FROM Dynamic order by time desc")
    DataSource.Factory<Integer,Dynamic> findAll(); //根据名字加载动漫

    @Insert
    long insertOneAnime(Dynamic anime); //插入一条信息

    @Insert
    void insertMultiAnimes(Dynamic... animes); //插入多条信息

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUsers(Dynamic... animes); //更新动漫信息，当有冲突时则进行替代

    @Delete
    void deleteAnime(Dynamic anime);
}
