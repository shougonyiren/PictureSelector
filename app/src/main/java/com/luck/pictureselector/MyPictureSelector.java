package com.luck.pictureselector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.luck.pictureselector.ui.mypictureselector.MyPictureSelectorFragment;

public class MyPictureSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_picture_selector_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MyPictureSelectorFragment.newInstance())
                    .commitNow();
        }
    }
}
