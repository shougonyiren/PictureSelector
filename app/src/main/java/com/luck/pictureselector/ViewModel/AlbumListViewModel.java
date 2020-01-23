package com.luck.pictureselector.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;
import androidx.room.Room;

import com.liuaho.repository.Dynamic;
import com.luck.pictureselector.App;
import com.luck.pictureselector.AppDatabase;
import com.luck.pictureselector.MyApp;

public class AlbumListViewModel extends ViewModel {
    private MyApp app=new MyApp();
    private DynamicDao dynamicDao;

    public AlbumListViewModel() {
    }

    public   LiveData<PagedList<Dynamic>> pagedListLiveData;
    public  PagedList.Config config=  new PagedList.Config.Builder().setPageSize(15)//每次加载的数据数量15
    //距离本页数据几个时候开始加载下一页数据(例如现在加载10个数据,设置prefetchDistance为2,则滑到第八个数据时候开始加载下一页数据).
                .setPrefetchDistance(15)//15
    //这里设置是否设置PagedList中的占位符,如果设置为true,我们的数据数量必须固定,由于网络数据数量不固定,所以设置false.
                .setEnablePlaceholders(true).setInitialLoadSizeHint(15)//15
                .build();
    public AlbumListViewModel(DynamicDao dynamicDao) {
        this.dynamicDao=dynamicDao;
        initPagedList();
        /*pagedListLiveData = new LivePagedListBuilder( ,
                15);*/
    }

    public LiveData<PagedList<Dynamic>> getPagedListLiveData() {
        initPagedList();
        return pagedListLiveData;
    }

    private void initPagedList() {
        final PositionalDataSource<Dynamic> positionalDataSource = new PositionalDataSource<Dynamic>(){

            @Override
            public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Dynamic> callback) {
                //dynamicDao.getAll().loadInitial(params,callback);
            }

            @Override
            public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Dynamic> callback) {
               //dynamicDao.getAll().loadRange(params,callback);
            }
        } ;

        // 构建LiveData
       pagedListLiveData =new LivePagedListBuilder( AppDatabase.getInstance(App.getInstance())
.dynamicDao().findAll(),15).build() ; /*new LivePagedListBuilder(new PageDataSourceFactory(positionalDataSource)//自己定义
                , new PagedList.Config.Builder().setPageSize(8)//每次加载的数据数量15
               //距离本页数据几个时候开始加载下一页数据(例如现在加载10个数据,设置prefetchDistance为2,则滑到第八个数据时候开始加载下一页数据).
               .setPrefetchDistance(2)//15
               //这里设置是否设置PagedList中的占位符,如果设置为true,我们的数据数量必须固定,由于网络数据数量不固定,所以设置false.
               .setEnablePlaceholders(false).setInitialLoadSizeHint(6)//15
               .build()
               ).build();*/

    }
}
