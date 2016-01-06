package br.com.dgimenes.nasapic.service.web;

import java.util.List;

import br.com.dgimenes.nasapic.model.api.SpacePicDTO;
import retrofit.Callback;
import retrofit.http.GET;

public interface NasaPicServerWebservice {

    @GET("/feed/list")
    void getFeed(Callback<List<SpacePicDTO>> callback);
}
