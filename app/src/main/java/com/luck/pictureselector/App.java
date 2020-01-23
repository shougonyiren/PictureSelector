package com.luck.pictureselector;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.room.Room;

import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.crash.PictureSelectorCrashUtils;


/**
 * @author：luck
 * @date：2019-12-03 22:53
 * @describe：Application
 */

public class App extends Application implements IApp, CameraXConfig.Provider {
    private AppDatabase appDB;
    private static  App mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        /** PictureSelector日志管理配制开始 **/
        // PictureSelector 绑定监听用户获取全局上下文或其他...
        PictureAppMaster.getInstance().setApp(this);
        // PictureSelector Crash日志监听
        PictureSelectorCrashUtils.init((t, e) -> {
            // Crash之后的一些操作可再此处理，没有就忽略...

        });
        /** PictureSelector日志管理配制结束 **/

        mInstance = this;
/*        appDB = Room.databaseBuilder(this,AppDatabase.class,"Dynamic")
                .addMigrations()
                .allowMainThreadQueries()
                .build();*/
    }

    @Override
    public Context getAppContext() {
        return this;
    }
    public static App getInstance(){
        return mInstance;
    }

    public AppDatabase getAppDB(){
        if(appDB==null){
            appDB=  Room.databaseBuilder(this,AppDatabase.class,"Dynamic")
                    .addMigrations()
                    .allowMainThreadQueries()
                    .build();
        }
        return appDB;
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
