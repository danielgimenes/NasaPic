package br.com.dgimenes.nasapic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import br.com.dgimenes.nasapic.model.api.ApodDTO;

public class APOD implements Parcelable {
    private String url;
    private String explanation;
    private String title;
    private Date date;

    public APOD(ApodDTO apodDTO, Date date) {
        this.url = apodDTO.getUrl();
        this.explanation = apodDTO.getExplanation();
        this.title = apodDTO.getTitle();
        this.date = date;
    }

    public APOD() {}

    protected APOD(Parcel in) {
        url = in.readString();
        explanation = in.readString();
        title = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<APOD> CREATOR = new Creator<APOD>() {
        @Override
        public APOD createFromParcel(Parcel in) {
            return new APOD(in);
        }

        @Override
        public APOD[] newArray(int size) {
            return new APOD[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(explanation);
        dest.writeString(title);
        dest.writeLong(date.getTime());
    }

    public static class Builder {
        private final APOD instance;

        public Builder() {
            instance = new APOD();
        }

        public Builder url(String url) {
            instance.url = url;
            return this;
        }

        public Builder explanation(String explanation) {
            instance.explanation = explanation;
            return this;
        }

        public Builder title(String title) {
            instance.title = title;
            return this;
        }

        public Builder date(Date date) {
            instance.date = date;
            return this;
        }

        public APOD build() {
            return instance;
        }
    }
}
