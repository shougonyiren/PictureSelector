package com.luck.pictureselector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * Funcation : ---
 * Creater : 王哲
 * Created by shootbox on 2016/3/26.
 */
public class MyImageDialog extends Dialog{

    private Window window = null;
    private ImageView iv;
    private String bms;
    public MyImageDialog(Context context, boolean cancelable,
                         DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public MyImageDialog(Context context, int cancelable,int x,int y,String bm) {
        super(context, cancelable);
        windowDeploy(x, y);
        bms = bm;

    }
    public MyImageDialog(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        //初始化布局
        View loadingview= LayoutInflater.from(getContext()).inflate(R.layout.imagedialogview,null);
        iv=(ImageView) loadingview.findViewById(R.id.imageview_head_big);
        Glide
                .with(getContext())
                .load(bms)
                .thumbnail(1f)
               // .thumbnail( 0.1f )
                .into(iv);
        //iv.setImageBitmap(bms);
        //设置dialog的布局
        setContentView(loadingview);
        //如果需要放大或者缩小时的动画，可以直接在此出对loadingview或iv操作，在下面SHOW或者dismiss中操作
        super.onCreate(savedInstanceState);
    }

    //设置窗口显示
    public void windowDeploy(int x, int y){
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.app_color_black); //设置对话框背景为透明
/*        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
//            wl.alpha = 0.6f; //设置透明度
//            wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);*/
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        // 前2 个flag设置dialog 显示到状态栏    第三个设置点击dialog以外的蒙层 不抢夺焦点  响应点击事件
        lp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ;
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
    }

    public void show() {
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        super.show();
    }
    public void dismiss() {
        super.dismiss();
    }
}