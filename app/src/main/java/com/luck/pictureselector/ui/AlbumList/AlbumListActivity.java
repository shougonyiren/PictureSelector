package com.luck.pictureselector.ui.AlbumList;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liuaho.repository.Dynamic;
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
import android.widget.ImageView;

import java.util.ArrayList;

public class AlbumListActivity extends AppCompatActivity implements  AlbumOnclick {

    private AlbumListViewModel viewModel;
    private RecyclerView recyclerView;
    private AlbumListAdapter albumListAdapter;

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
        albumListAdapter.setAlbumOnclickListener(this::Onclick);
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
    public void Onclick(ImageView[] imageViews, ArrayList<String> paths, int position) {
/*
        Zoomy.Builder builder = new Zoomy.Builder(this).target(imageViews[position]);
        builder.register();*/
        //startImageActivity(this,imageViews,(String[]) paths.toArray(),position);
    }
}
