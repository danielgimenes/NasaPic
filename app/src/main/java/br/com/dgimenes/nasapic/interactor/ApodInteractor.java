package br.com.dgimenes.nasapic.interactor;

import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.webservice.ApodDTO;
import br.com.dgimenes.nasapic.webservice.NasaWebservice;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApodInteractor {
    private static final String NASA_API_KEY = "DEMO_KEY";
    private static final String NASA_API_BASE_URL = "https://api.nasa.gov/planetary";
    private final NasaWebservice nasaWebservice;

    public ApodInteractor() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(NASA_API_BASE_URL)
                .build();
        this.nasaWebservice = restAdapter.create(NasaWebservice.class);
    }

    public void getNasaApodPictureURI(final OnFinishListener<String> onFinishListener) {

        nasaWebservice.getAPOD(NASA_API_KEY, false, new Callback<ApodDTO>() {

            @Override
            public void success(ApodDTO apodDTO, Response response) {
                if (apodDTO.getMediaType() != "image") {
                    onFinishListener.onError(new APODIsNotAPictureException());
                    return;
                }
                onFinishListener.onSuccess(apodDTO.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}