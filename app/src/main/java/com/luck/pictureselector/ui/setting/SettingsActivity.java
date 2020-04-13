package com.luck.pictureselector.ui.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.dialog.PictureLoadingDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.widget.FolderPopWindow;
import com.luck.pictureselector.R;
import com.luck.pictureselector.service.ImageAnalysisIntentService;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        PictureAlbumDirectoryAdapter.OnItemClickListener {
    //todo 目录图片
    private PictureSelectionConfig config;
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private PictureLoadingDialog mLoadingDialog;

    protected List<LocalMedia> images = new ArrayList<>();
    protected List<LocalMediaFolder> foldersList = new ArrayList<>();

    private FolderPopWindow folderWindow;
    private ImageView mIvArrow;
    private RelativeLayout image_analysis_bg;
    private TextView test;


    private  TextView textView;
    private  LocalMediaLoader mediaLoader;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG:
                    showPleaseDialog();
                    break;
                case DISMISS_DIALOG:
                    dismissDialog();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        isCheckConfigNull();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=new Intent(this,ImageAnalysisIntentService.class);
        test=findViewById(R.id.test1111);
        test.setOnClickListener(this);
        mIvArrow=findViewById(R.id.ivArrow);
        image_analysis_bg=findViewById(R.id.image_analysis_bg);
        textView=findViewById(R.id.tv_image_analysis);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllMediaData();
                //Todo 执行service
                //startService(intent);
            }
        });
        folderWindow = new FolderPopWindow(this, config);
        folderWindow.setArrowImageView(mIvArrow);
        folderWindow.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.test1111){
            folderWindow.showAsDropDown(image_analysis_bg);
        }
/*        if (id == com.luck.picture.lib.R.id.picture_title || id == com.luck.picture.lib.R.id.ivArrow) {
            if (folderWindow.isShowing()) {
                folderWindow.dismiss();
            } else {
                if (images != null && images.size() > 0) {
                    folderWindow.showAsDropDown(image_analysis_bg);
                }
            }
        }*/
    }

    /**
     * 加载数据
     */
    private void loadAllMediaData() {
        if (PermissionChecker
                .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                PermissionChecker
                        .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            readLocalMedia();
        } else {
            PermissionChecker.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    /**
     * get LocalMedia s
     */
    private void readLocalMedia() {
        mHandler.sendEmptyMessage(SHOW_DIALOG);
        if (mediaLoader == null) {
            mediaLoader = new LocalMediaLoader(this, config);
        }
        mediaLoader.loadAllMedia();
        mediaLoader.setCompleteListener(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                if (folders.size() > 0) {
                    foldersList = folders;
                    LocalMediaFolder folder = folders.get(0);
                    folder.setChecked(true);
                    List<LocalMedia> result = folder.getImages();
                    if (images == null) {
                        images = new ArrayList<>();
                    }
                    // 正常情况下    当有些机型会出现拍照完，相册列表不及时刷新问题  解决-》 PictureSelectorActivity
                    images = result;
                    folderWindow.bindFolder(folders);
                }
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
            }

            @Override
            public void loadMediaDataError() {
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
                Toast.makeText(getBaseContext(), getString(com.luck.picture.lib.R.string.picture_data_exception),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }



    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        if (!isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new PictureLoadingDialog(this);
            }
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog.show();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        if (!isFinishing()) {
            try {
                if (mLoadingDialog != null
                        && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            } catch (Exception e) {
                mLoadingDialog = null;
                e.printStackTrace();
            }
        }
    }

    private void isCheckConfigNull() {
        if (config == null) {
            config = PictureSelectionConfig.getInstance();
        }
    }

    @Override
    public void onItemClick(boolean isCameraFolder, String folderName, List<LocalMedia> images) {
        //todo 点击
    }
}