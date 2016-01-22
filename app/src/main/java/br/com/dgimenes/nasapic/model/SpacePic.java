package br.com.dgimenes.nasapic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.dgimenes.nasapic.model.api.SpacePicDTO;

public class SpacePic implements Parcelable {

    private String hdImageUrl;

    private String previewImageUrl;

    private Date originallyPublishedAt;

    private String description;

    private String title;

    private Date publishedAt;

    private String source;

    public SpacePic(String hdImageUrl, String previewImageUrl, Date originallyPublishedAt,
                    String description, String title, Date publishedAt, String source) {
        this.hdImageUrl = hdImageUrl;
        this.previewImageUrl = previewImageUrl;
        this.originallyPublishedAt = originallyPublishedAt;
        this.description = description;
        this.title = title;
        this.publishedAt = publishedAt;
        this.source = source;
    }

    public SpacePic(SpacePicDTO dto) {
        this.hdImageUrl = dto.getHdImageUrl();
        this.previewImageUrl = dto.getPreviewImageUrl();
        this.originallyPublishedAt = dto.getOriginallyPublishedAt();
        this.description = dto.getDescription();
        this.title = dto.getTitle();
        this.publishedAt = dto.getPublishedAt();
        this.source = dto.getSource();
    }

    public SpacePic() {}

    private static final java.lang.String DATEFORMAT = "yyyy-MM-dd";

    protected SpacePic(Parcel in) {
        this.hdImageUrl = in.readString();
        this.previewImageUrl = in.readString();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
            this.originallyPublishedAt = dateFormat.parse(in.readString());
            this.publishedAt = dateFormat.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.description = in.readString();
        this.title = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<SpacePic> CREATOR = new Parcelable.Creator<SpacePic>() {
        @Override
        public SpacePic createFromParcel(Parcel in) {
            return new SpacePic(in);
        }

        @Override
        public SpacePic[] newArray(int size) {
            return new SpacePic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        dest.writeString(this.hdImageUrl);
        dest.writeString(this.previewImageUrl);
        dest.writeString(dateFormat.format(this.originallyPublishedAt));
        dest.writeString(dateFormat.format(this.publishedAt));
        dest.writeString(this.description);
        dest.writeString(this.title);
        dest.writeString(this.source);
    }

    public String getHdImageUrl() {
        return hdImageUrl;
    }

    public void setHdImageUrl(String hdImageUrl) {
        this.hdImageUrl = hdImageUrl;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public Date getOriginallyPublishedAt() {
        return originallyPublishedAt;
    }

    public void setOriginallyPublishedAt(Date originallyPublishedAt) {
        this.originallyPublishedAt = originallyPublishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static String getDATEFORMAT() {
        return DATEFORMAT;
    }

    public static Creator<SpacePic> getCREATOR() {
        return CREATOR;
    }
}
