package br.com.dgimenes.nasapic.service;

import android.content.Context;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class DefaultPicasso {

    public static Picasso get(Context context, Picasso.Listener errorListener) {
        Picasso.Builder builder = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context, Integer.MAX_VALUE));
        if (errorListener != null) {
            builder = builder.listener(errorListener);
        }
        return builder.build();
    }
}
