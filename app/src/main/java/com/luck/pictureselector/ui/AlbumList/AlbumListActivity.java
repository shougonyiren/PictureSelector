package com.luck.pictureselector.ui.AlbumList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liuaho.repository.Dynamic;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.pictureselector.GlideEngine;
import com.luck.pictureselector.MainActivity;
import com.luck.pictureselector.R;
import com.luck.pictureselector.ViewModel.AlbumListViewModel;
import com.luck.pictureselector.ui.mypictureselector.MyPictureSelectorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

public class AlbumListActivity extends AppCompatActivity implements  AlbumOnclick {

    private AlbumListViewModel viewModel;
    private RecyclerView recyclerView;
    private AlbumListAdapter albumListAdapter;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        appBarLayout=findViewById(R.id.appBarLayout2);
        viewModel=  ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AlbumListViewModel.class);
        recyclerView=findViewById(R.id.dynamic_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        albumListAdapter=new AlbumListAdapter(new DiffUtil.ItemCallback<Dynamic>() {

            @Override
            public boolean areItemsTheSame(@NonNull Dynamic oldItem, @NonNull Dynamic newItem) {
                return false;//oldItem.getTime().equals(newItem.getTime());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Dynamic oldItem, @NonNull Dynamic newItem) {
                return false;//oldItem.getConent().contains(newItem.getConent()) && oldItem.getLocalMediaList().equals(newItem.getLocalMediaList());
            }
        },this);
        albumListAdapter.setAlbumOnclickListener(( paths, position) -> Onclick(paths, position));
        recyclerView.setAdapter(albumListAdapter);
        viewModel.getPagedListLiveData().observe(this, new Observer<PagedList<Dynamic>>() {
            @Override
            public void onChanged(PagedList<Dynamic> dynamics) {
                albumListAdapter.submitList(dynamics);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyPictureSelectorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void Onclick(List<LocalMedia> paths, int position) {
        PictureSelector.create(this)
                .themeStyle(R.style.picture_default_style) // xml设置主题
               // .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                //.bindCustomPlayVideoCallback(callback)// 自定义播放回调控制，用户可以使用自己的视频播放界面
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .openExternalPreview(position, paths);
/*
        Zoomy.Builder builder = new Zoomy.Builder(this).target(imageViews[position]);
        builder.register();*/
        //startImageActivity(this,imageViews,(String[]) paths.toArray(),position);
    }
}
