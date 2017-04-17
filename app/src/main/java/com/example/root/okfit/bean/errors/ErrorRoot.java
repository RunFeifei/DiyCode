package com.example.root.okfit.bean.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PengFeifei on 17-4-17.
 */

public class ErrorRoot {

    @Expose
    @SerializedName("content")
    @JsonProperty("content")
    private ErrorContent errorContent;

    @Expose
    @JsonProperty
    private String result;

    @Expose
    @JsonProperty
    private String[] errors;

    public ErrorContent getErrorContent() {
        return this.errorContent;
    }

    public String getResult() {
        return this.result;
    }

}
