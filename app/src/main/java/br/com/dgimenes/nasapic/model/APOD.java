package br.com.dgimenes.nasapic.model;

import java.util.Date;

public class APOD {
    private String url;
    private String explanation;
    private String title;
    private Date date;

    public APOD(String url, String explanation, String title, Date date) {
        this.url = url;
        this.explanation = explanation;
        this.title = title;
        this.date = date;
    }

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
}
