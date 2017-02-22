package com.example.home.header;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * 이미지 프로세싱
 */
public class ImageProc {
    private static ImageProc ourInstance = new ImageProc();

    public static ImageProc getInstance() {
        return ourInstance;
    }

    private ImageProc() {
    }

    // =============================================================

    // UIL :  Universal Image-Loader
    ImageLoader imageLoader;
    ImageLoaderConfiguration configuration;
    DisplayImageOptions displayImageOptions;

    // 초기화
    public ImageLoader getImageLoader(Context context) {

        if(imageLoader == null){
            // 이미 싱글톤이므로 굳이 변수로 받지 않아도 된다.
            imageLoader = ImageLoader.getInstance();
            configuration = new ImageLoaderConfiguration.Builder(context).build();
            imageLoader.init(configuration);

            displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return imageLoader;
    }

    // 이미지 드로잉
    public void drawImage(String url, ImageView imageView){
        imageLoader.displayImage(url, imageView, displayImageOptions);
    }
}
