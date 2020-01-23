package com.luck.pictureselector.ui.AlbumList;

import android.widget.ImageView;

import java.util.ArrayList;

public interface AlbumOnclick {
    void Onclick(ImageView[] imageViews, ArrayList<String> paths, int position);
}
