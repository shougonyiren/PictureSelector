package com.luck.pictureselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.pictureselector.ui.mypictureselector.MyPictureSelectorFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AlbumListActivity extends AppCompatActivity {

    private int chooseMode = PictureMimeType.ofAll();//选择模式
    private List<LocalMedia> selectList = new ArrayList<>();

    private MyPictureSelectorFragment fragment;
    private FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        layout=findViewById(R.id.select_layout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在部分低端手机，调用单独拍照时内存不足时会导致activity被回收，所以不重复创建fragment
                if (savedInstanceState == null) {
                    // 添加显示第一个fragment
                    fragment = new MyPictureSelectorFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.select_layout, fragment,
                            PictureConfig.EXTRA_FC_TAG).show(fragment)
                            .commit();
                    layout.bringToFront();
                } else {
                    fragment = (MyPictureSelectorFragment) getSupportFragmentManager()
                            .findFragmentByTag(PictureConfig.EXTRA_FC_TAG);
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE:
                // 存储权限
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        PictureFileUtils.deleteCacheDirFile(AlbumListActivity.this, PictureMimeType.ofImage());
                    } else {
                        Toast.makeText(AlbumListActivity.this,
                                getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
