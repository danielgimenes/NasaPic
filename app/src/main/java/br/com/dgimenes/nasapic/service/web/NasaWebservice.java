package br.com.dgimenes.nasapic.service.web;

import br.com.dgimenes.nasapic.model.api.ApodDTO;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NasaWebservice {

    @GET("/apod")
    void getAPOD(@Query("api_key") String nasaApiKey,
                 @Query("concept_tags") boolean conceptTags,
                 @Query("date") String formattedDate,
                 Callback<ApodDTO> callback);
}
