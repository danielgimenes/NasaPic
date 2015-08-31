package br.com.dgimenes.nasapic.interactor;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.dgimenes.nasapic.exception.APODIsNotAPictureException;
import br.com.dgimenes.nasapic.webservice.ApodDTO;
import br.com.dgimenes.nasapic.webservice.NasaWebservice;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApodInteractor {
    private static final String NASA_API_KEY = "biwbr55t29bUSURh2hMbkccNkpvRoNyVi8XBHxm1";
    private static final String NASA_API_BASE_URL = "https://api.nasa.gov/planetary";
    private static final String NASA_API_DATE_FORMAT = "yyyy-MM-dd";
    private final NasaWebservice nasaWebservice;

    public ApodInteractor() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(NASA_API_BASE_URL)
                .build();
        this.nasaWebservice = restAdapter.create(NasaWebservice.class);
    }

    public void getNasaApodPictureURI(Date date,
                                      final OnFinishListener<String> onFinishListener) {
        String formattedDate = new SimpleDateFormat(NASA_API_DATE_FORMAT).format(date);
        nasaWebservice.getAPOD(NASA_API_KEY, false, formattedDate, new Callback<ApodDTO>() {

            @Override
            public void success(ApodDTO apodDTO, Response response) {
                if (apodDTO.getMediaType() == null) {
                    String errorMessage = "Invalid response. Response: " +
                            response.getStatus() + " " + response.getReason();
                    Log.e(ApodInteractor.class.getSimpleName(), errorMessage);
                    Toast.makeText(null, errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!apodDTO.getMediaType().equals("image")) {
                    onFinishListener.onError(new APODIsNotAPictureException());
                    return;
                }
                onFinishListener.onSuccess(apodDTO.getUrl());
            }

            @Override
            public void failure(RetrofitError error) {
                onFinishListener.onError(error.getCause());
            }
        });
    }
}