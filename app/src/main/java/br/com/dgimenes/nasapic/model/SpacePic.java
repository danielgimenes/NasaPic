package br.com.dgimenes.nasapic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import br.com.dgimenes.nasapic.model.api.SpacePicDTO;

public class SpacePic implements Parcelable {
    private String url;
    private String explanation;
    private String title;
    private Date date;

    public SpacePic(SpacePicDTO spacePicDTO) {
        this.url = spacePicDTO.getUrl();
        this.explanation = spacePicDTO.getExplanation();
        this.title = spacePicDTO.getTitle();
        this.date = spacePicDTO.getDate();
    }

    public SpacePic() {}

    protected SpacePic(Parcel in) {
        url = in.readString();
        explanation = in.readString();
        title = in.readString();
        date = new Date(in.readLong());
    }

    public static final Parcelable.Creator<APOD> CREATOR = new Parcelable.Creator<APOD>() {
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
        private final SpacePic instance;

        public Builder() {
            instance = new SpacePic();
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

        public SpacePic build() {
            return instance;
        }
    }
}
