package br.com.dgimenes.nasapic.service;

import android.content.Context;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class DefaultPicasso {

    private static final long PICASSO_CACHE_IN_BYTES = 20 * 1024 * 1024; // 20 MB

    public static Picasso get(Context context, Picasso.Listener errorListener) {
        Picasso.Builder builder = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context, PICASSO_CACHE_IN_BYTES));
        if (errorListener != null) {
            builder = builder.listener(errorListener);
        }
        return builder.build();
    }
}
