package br.com.dgimenes.nasapic.service.interactor;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public abstract class RetrofitWithCacheInteractor {
    private static final long HTTP_CACHE_SIZE_IN_BYTES = 2048;
    private final RestAdapter restAdapter;

    public RetrofitWithCacheInteractor(Context context, String apiBaseUrl) {
        this.restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(apiBaseUrl)
                .setConverter(
                        new GsonConverter(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .setClient(createOKHttpClient(context))
                .build();
    }

    private Client createOKHttpClient(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(createHttpClientCache(context));
        return new OkClient(okHttpClient);
    }

    public Cache createHttpClientCache(Context context) {
        try {
            File cacheDir = context.getDir("service_api_cache", Context.MODE_PRIVATE);
            return new Cache(cacheDir, HTTP_CACHE_SIZE_IN_BYTES);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }
}
