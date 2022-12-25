package com.example.racegame.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.racegame.R;

/**
 * class ImageLoader is to assist us to load image with glide
 */
public class ImageLoader {

    private static ImageLoader instance;
    private static Context appContext;

    /**
     * ImageLoader constructor
     * @param context
     */
    private ImageLoader(Context context) {
        appContext = context;
    }

    /**
     * getInstance method return instance
     * @return instance-ImageLoader
     */
    public static ImageLoader getInstance() {
        return instance;
    }

    /**
     * initInstance method init this object
     * @param context-Context
     */
    public static void initInstance(Context context) {
        if (instance == null)
            instance = new ImageLoader(context);
    }

    /**
     * load method load IMG by using Glide
     * link is id of IMG
     * @param link-int
     * @param imageView-ImageView
     */
    public void load (int link, ImageView imageView){
        Glide.with(appContext).load(link).centerCrop()
                .placeholder(R.drawable.ic_launcher_background).into(imageView);
    }

}
