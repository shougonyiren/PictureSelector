package com.luck.pictureselector.ui.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.dialog.PictureLoadingDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.pictureselector.R;
import com.luck.pictureselector.service.ImageAnalysisIntentService;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private PictureSelectionConfig config;
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private PictureLoadingDialog mLoadingDialog;

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
        textView=findViewById(R.id.tv_image_analysis);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 执行service
                startService(intent);
            }
        });
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
/*        mediaLoader.setCompleteListener(new LocalMediaLoader.LocalMediaLoadListener() {
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
                    // 这里解决有些机型会出现拍照完，相册列表不及时刷新问题
                    // 因为onActivityResult里手动添加拍照后的照片，
                    // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
                    int currentSize = images.size();
                    int resultSize = result.size();
                    oldCurrentListSize = oldCurrentListSize + currentSize;
                    if (resultSize >= currentSize) {
                        if (currentSize > 0 && currentSize < resultSize && oldCurrentListSize != resultSize) {
                            // 这种情况多数是由于拍照导致Activity和数据被回收数据不一致
                            images.addAll(result);
                            // 更新相机胶卷目录
                            LocalMedia media = images.get(0);
                            folder.setFirstImagePath(media.getPath());
                            folder.getImages().add(0, media);
                            folder.setCheckedNum(1);
                            folder.setImageNum(folder.getImageNum() + 1);
                            // 更新相片所属目录
                            updateMediaFolder(foldersList, media);
                        } else {
                            // 正常情况下
                            images = result;
                        }
                        folderWindow.bindFolder(folders);
                    }
                }
                if (mAdapter != null) {
                    mAdapter.bindImagesData(images);
                    boolean isEmpty = images.size() > 0;
                    if (!isEmpty) {
                        mTvEmpty.setText(getString(com.luck.picture.lib.R.string.picture_empty));
                        mTvEmpty.setCompoundDrawablesRelativeWithIntrinsicBounds
                                (0, com.luck.picture.lib.R.drawable.picture_icon_no_data, 0, 0);
                    }
                    mTvEmpty.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                }
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
            }

            @Override
            public void loadMediaDataError() {
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mTvEmpty.setCompoundDrawablesRelativeWithIntrinsicBounds
                            (0, com.luck.picture.lib.R.drawable.picture_icon_data_error, 0, 0);
                }
                mTvEmpty.setText(getString(com.luck.picture.lib.R.string.picture_data_exception));
                mTvEmpty.setVisibility(images.size() > 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });*/

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

}