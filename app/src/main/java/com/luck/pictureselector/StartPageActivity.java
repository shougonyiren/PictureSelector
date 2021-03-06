package com.luck.pictureselector;

import android.content.Intent;
import android.os.Bundle;

import com.liuaho.base.BaseActivity;
import com.luck.pictureselector.ui.AlbumList.AlbumListActivity;

public class StartPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_start_page);
        startActivity(new Intent(this, AlbumListActivity.class));//设置启动页结束要打开的Activity
        finish();
/*        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1000);//使程序休眠一秒
                    Intent it = new Intent(getApplicationContext(), AlbumListActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程*/
    }
}
