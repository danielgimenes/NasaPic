package br.com.dgimenes.nasapic.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SpacePicDTO {

    @SerializedName("hd-image-url")
    private String hdImageUrl;

    @SerializedName("preview-image-url")
    private String previewImageUrl;

    @SerializedName("originally-published-at")
    private Date originallyPublishedAt;

    private String description;

    private String title;

    @SerializedName("published-at")
    private Date publishedAt;

    private String source;

    public SpacePicDTO() {
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
}
