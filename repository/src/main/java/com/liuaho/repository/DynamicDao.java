package com.liuaho.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;
@Dao
public interface DynamicDao {
    @Query("SELECT * FROM  Dynamic order by time desc")
    List<Dynamic> getAllAnime(); //加载所有动漫数据

/*    @Query("SELECT * FROM Dynamic WHERE name = :name")
    LocalMedia loadAnimeByName(String name); //根据名字加载动漫*/

    @Insert
    void insertOneAnime(Dynamic anime); //插入一条动漫信息

    @Insert
    void insertMultiAnimes(Dynamic... animes); //插入多条动漫信息

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUsers(Dynamic... animes); //更新动漫信息，当有冲突时则进行替代

    @Delete
    void deleteAnime(Dynamic anime);
}
