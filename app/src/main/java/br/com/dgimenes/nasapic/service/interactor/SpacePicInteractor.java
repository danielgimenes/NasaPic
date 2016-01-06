package br.com.dgimenes.nasapic.service.interactor;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.com.dgimenes.nasapic.model.SpacePic;
import br.com.dgimenes.nasapic.model.api.SpacePicDTO;
import br.com.dgimenes.nasapic.service.web.NasaPicServerWebservice;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpacePicInteractor extends RetrofitWithCacheInteractor {
    private static String NASAPICSERVER_BASEURL = "http://nasapicserver.ddns.net:9123";
    private final NasaPicServerWebservice webservice;

    private WeakReference<Context> contextWeakReference;

    public SpacePicInteractor(Context context) {
        super(context, NASAPICSERVER_BASEURL);
        contextWeakReference = new WeakReference<>(context);
        webservice = getRestAdapter().create(NasaPicServerWebservice.class);
    }

    public void getFeed(int page, final OnFinishListener<List<SpacePic>> onFinishListener) {
        webservice.getFeed(new Callback<List<SpacePicDTO>>() {
            @Override
            public void success(List<SpacePicDTO> spacePicDTOs, Response response) {
                List<SpacePic> spacePics = new ArrayList<>();
                for (SpacePicDTO spacePicDTO : spacePicDTOs) {
                    spacePics.add(new SpacePic(spacePicDTO));
                }
                onFinishListener.onSuccess(spacePics);
            }

            @Override
            public void failure(RetrofitError error) {
                onFinishListener.onError(error);
            }
        });
    }
}
