package br.com.dgimenes.nasapic.service.web;

import br.com.dgimenes.nasapic.model.api.FeedDTO;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NasaPicServerWebservice {

    @GET("/feed/list")
    void getFeed(@Query("page") int page, Callback<FeedDTO> callback);
}
