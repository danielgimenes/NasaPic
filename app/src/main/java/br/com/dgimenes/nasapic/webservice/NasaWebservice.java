package br.com.dgimenes.nasapic.webservice;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NasaWebservice {

    @GET("/apod")
    void getAPOD(@Query("api_key") String nasaApiKey,
                 @Query("concept_tags") boolean conceptTags,
                 Callback<ApodDTO> callback);
}
