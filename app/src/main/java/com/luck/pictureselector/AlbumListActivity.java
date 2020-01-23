package com.luck.pictureselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.liuaho.repository.Dynamic;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.pictureselector.ViewModel.AlbumListViewModel;
import com.luck.pictureselector.ui.mypictureselector.MyPictureSelectorActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AlbumListActivity extends AppCompatActivity {

    private int chooseMode = PictureMimeType.ofAll();//选择模式
    private List<LocalMedia> selectList = new ArrayList<>();
    private AlbumListViewModel viewModel;
    private RecyclerView recyclerView;
    private AlbumListAdapter albumListAdapter;
    /*private AppDatabase db = Room.databaseBuilder(getApplicationContext(),
            AppDatabase.class, "roomDemo-database")
            //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
            //他可能造成主线程lock以及anr
            //所以我们的操作都是在新线程完成的
            // .allowMainThreadQueries()
            .build();*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
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
}
