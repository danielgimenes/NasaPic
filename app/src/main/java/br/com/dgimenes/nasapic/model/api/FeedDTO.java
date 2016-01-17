package br.com.dgimenes.nasapic.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedDTO {
    @SerializedName("paging")
    private PagingInfo pagingInfo;

    @SerializedName("data")
    private List<SpacePicDTO> spacePics;

    public List<SpacePicDTO> getSpacePics() {
        return spacePics;
    }

    public void setSpacePics(List<SpacePicDTO> spacePics) {
        this.spacePics = spacePics;
    }

    public PagingInfo getPagingInfo() {
        return pagingInfo;
    }

    public void setPagingInfo(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
    }
}
