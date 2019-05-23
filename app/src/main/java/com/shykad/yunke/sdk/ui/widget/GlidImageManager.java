package com.shykad.yunke.sdk.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shykad.yunke.sdk.utils.LogUtils;

import java.io.File;

/**
 * Create by wanghong.he on 2019/3/6.
 * description：
 */
public class GlidImageManager {
    private static GlidImageManager instance;

    private GlidImageManager(){

    }

    public static GlidImageManager getInstance(){
        if(instance == null){
            synchronized (GlidImageManager.class){
                if(instance == null) {
                    instance = new GlidImageManager();
                }
            }
        }
        return instance;
    }

    public void loadImageUri(Context context, String imgUrl, ImageView imageView, int defaultImg) {

        Glide.with(context)                             //配置上下文
                .load(Uri.fromFile(new File(imgUrl)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(defaultImg)           //设置错误图片
                .placeholder(defaultImg)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }


    public void loadImage(Context context, int resId, ImageView imageView, int defaultImg) {
        Glide.with(context)                             //配置上下文
                .load(resId)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(defaultImg)           //设置错误图片
                .placeholder(defaultImg)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    public void loadImageView(Context context, String imageUrl,ImageView imageView, int defaultImg){
        if (context!=null){
            try {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(defaultImg)
                        .error(defaultImg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.i("Picture loading failed,context is null\n"+e.getMessage());
            }
        }else {
            LogUtils.i("Picture loading failed,context is null");
        }

    }

    public interface GlideLoadBitmapCallback{
        void getBitmapCallback(Bitmap bitmap);
    }

    public void getBitmap(Context context, String uri,int defaultImg, final GlideLoadBitmapCallback callback) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .placeholder(defaultImg)
                .error(defaultImg)
                .centerCrop()
                .override(150, 150)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        callback.getBitmapCallback(bitmap);
                    }
                });
    }

    public void getBitmap(Context context, int resId,int defaultImg, final GlideLoadBitmapCallback callback) {
        Glide.with(context)
                .load(resId)
                .asBitmap()
                .placeholder(defaultImg)
                .error(defaultImg)
                .centerCrop()
                .override(150, 150)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        callback.getBitmapCallback(bitmap);
                    }
                });
    }
}
