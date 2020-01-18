package com.luck.pictureselector.ViewModel;

import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

import com.liuaho.repository.Dynamic;

public class PageDataSourceFactory extends DataSource.Factory {
    public PositionalDataSource<Dynamic> mPositionalDataSource;
    public PageDataSourceFactory(PositionalDataSource<Dynamic> positionalDataSource) {
        this.mPositionalDataSource = positionalDataSource;
    }

    @Override
    public DataSource create() {
        return mPositionalDataSource;
    }
}