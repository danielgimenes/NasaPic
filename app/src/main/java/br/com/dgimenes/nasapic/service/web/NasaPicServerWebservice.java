package br.com.dgimenes.nasapic.service.web;

import br.com.dgimenes.nasapic.model.api.FeedDTO;
import retrofit.Callback;
import retrofit.http.GET;

public interface NasaPicServerWebservice {

    @GET("/feed/list")
    void getFeed(Callback<FeedDTO> callback);
}
