package br.com.dgimenes.nasapic.model;

import java.util.Date;

public class APOD {
    private String url;
    private String explanation;
    private String title;
    private Date date;

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
