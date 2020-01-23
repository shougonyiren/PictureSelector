package com.luck.pictureselector;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class  Imageviewholder extends RecyclerView.ViewHolder{
    ImageView imageView;
    public Imageviewholder(@NonNull View itemView) {
        super(itemView);
        itemView=itemView.findViewById(R.id.item_image);
    }
}