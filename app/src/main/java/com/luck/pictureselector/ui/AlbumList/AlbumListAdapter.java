package com.luck.pictureselector.ui.AlbumList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.liuaho.repository.Dynamic;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.pictureselector.MyImageDialog;
import com.luck.pictureselector.R;
import com.luck.pictureselector.ui.ExtendTextview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AlbumListAdapter extends PagedListAdapter<Dynamic,AlbumListAdapter.AlbumViewHolder> {

    AlbumOnclick listener;
    //注册监听事件
    public void setAlbumOnclickListener(AlbumOnclick listener) {
        this.listener = listener;
    }
    Context context;
    protected AlbumListAdapter(@NonNull DiffUtil.ItemCallback<Dynamic> diffCallback) {
        super(diffCallback);
    }


    public AlbumListAdapter(@NonNull DiffUtil.ItemCallback<Dynamic> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    protected AlbumListAdapter(@NonNull AsyncDifferConfig<Dynamic> config) {
        super(config);
    }


    @NonNull
    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("创建viewholder","AlbumViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic,parent,false);
        AlbumViewHolder holder=new AlbumViewHolder(view);
        return  holder;
    }


    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Log.d("onBindViewHolder","AlbumViewHolder");
        holder.timetView.setText(Objects.requireNonNull(getItem(position)).getTime().toString());
        holder.extendTextview.setText(Objects.requireNonNull(getItem(position)).getConent());
        itemAdapter itemAdapter=new itemAdapter(getItem(position).getLocalMediaList());
        holder.recyclerView.setAdapter(itemAdapter);
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
            RecyclerView.LayoutManager manager = new GridLayoutManager(itemView.getContext(), 3);
            /*manager.setAutoMeasureEnabled(true);*/
            recyclerView.setLayoutManager(manager);
        }
    }
    private class  itemAdapter extends RecyclerView.Adapter<Imageviewholder>{
        private List<LocalMedia> paths;
       // private ImageView[] imageViews;
        @NonNull
        @Override
        public Imageviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("Imageviewholder","onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_layout,parent,false);
            Imageviewholder imageviewholder=new Imageviewholder(view);
            return imageviewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull Imageviewholder holder, int position) {
            Log.d("Imageviewholder","onBindViewHolder");
            Glide
                    .with( context )
                    .load(paths.get(position).getPath())
                    .thumbnail( 0.1f )
                    .into( holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.Onclick(paths,position);
                }
            });
        }

        public itemAdapter (List<LocalMedia> paths) {
            this.paths = paths;
        }

        @Override
        public int getItemCount() {
            return paths.size();
        }
    }
    public class  Imageviewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public Imageviewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_image);
        }
    }
    /**
     * 得到本地图片文件
     * @param context
     * @return
     */
    public static ArrayList<HashMap<String,String>> getAllPictures(Context context) {
        ArrayList<HashMap<String,String>> picturemaps = new ArrayList<>();
        HashMap<String,String> picturemap;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id
        Cursor cursor = cr.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                picturemap = new HashMap<>();
                picturemap.put("image_id_path",cursor.getInt(0)+"");
                picturemap.put("thumbnail_path",cursor.getString(1));
                picturemaps.add(picturemap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //再得到正常图片的path
        for (int i = 0;i<picturemaps.size();i++) {
            picturemap = picturemaps.get(i);
            String media_id = picturemap.get("image_id_path");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Audio.Media._ID+"="+media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    picturemap.put("image_id",cursor.getString(0));
                    picturemaps.set(i,picturemap);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return picturemaps;
    }
}
