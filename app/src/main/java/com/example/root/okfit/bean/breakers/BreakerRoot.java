package com.example.root.okfit.bean.breakers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PengFeifei on 17-4-17.
 */

public class BreakerRoot {

    @Expose
    @SerializedName("content")
    @JsonProperty("content")
    private BreakContent breakContent;

    @Expose
    @JsonProperty
    private String result;

    @Expose
    @JsonProperty
    private String[] errors;

    public BreakContent getErrorContent() {
        return this.breakContent;
    }

    public String getResult() {
        return this.result;
    }

}
