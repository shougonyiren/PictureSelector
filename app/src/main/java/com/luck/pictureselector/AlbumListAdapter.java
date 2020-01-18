package com.luck.pictureselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.liuaho.repository.Dynamic;
import com.luck.pictureselector.ui.ExtendTextview;

import java.util.List;

public class AlbumListAdapter extends PagedListAdapter<Dynamic,AlbumListAdapter.AlbumViewHolder> {
    Context context;
    private List<Dynamic> dynamicList;

    public AlbumListAdapter(@NonNull DiffUtil.ItemCallback<Dynamic> diffCallback, Context context, List<Dynamic> dynamicList) {
        super(diffCallback);
        this.context = context;
        this.dynamicList = dynamicList;
    }

    public AlbumListAdapter(@NonNull AsyncDifferConfig<Dynamic> config, Context context, List<Dynamic> dynamicList) {
        super(config);
        this.context = context;
        this.dynamicList = dynamicList;
    }

    protected AlbumListAdapter(@NonNull DiffUtil.ItemCallback<Dynamic> diffCallback) {
        super(diffCallback);
    }

    protected AlbumListAdapter(@NonNull AsyncDifferConfig<Dynamic> config) {
        super(config);
    }


    @NonNull
    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic,parent,false);
        return new AlbumViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.timetView.setText(dynamicList.get(position).getTime().toString());
        holder.extendTextview.setText(dynamicList.get(position).getConent());
    }


    @Override
    public int getItemCount() {
        return dynamicList.size();
    }
    public class  AlbumViewHolder extends RecyclerView.ViewHolder {
        ExtendTextview extendTextview;
        RecyclerView recyclerView;
        TextView timetView;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            extendTextview=itemView.findViewById(R.id.content_text);
            recyclerView=itemView.findViewById(R.id.rv_item_AlbumList);
            timetView=itemView.findViewById(R.id.dynamic_time_text);
        }
    }
}
