package br.com.dgimenes.nasapic.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApodDTO {
    private String url;
    @SerializedName("media_type")
    private String mediaType;
    private String explanation;
    private List<String> concepts;
    private String title;

    public ApodDTO(String url, String mediaType, String explanation, List<String> concepts, String title) {
        this.url = url;
        this.mediaType = mediaType;
        this.explanation = explanation;
        this.concepts = concepts;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<String> getConcepts() {
        return concepts;
    }

    public String getTitle() {
        return title;
    }
}
