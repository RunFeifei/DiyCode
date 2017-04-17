package com.example.root.okfit.bean.errors;

import com.example.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class ErrorItem implements Entity{

    @Expose
    @JsonProperty
    private String code;

    @Expose
    @SerializedName("zh-CN")
    @JsonProperty("zh-CN")
    private String zhCN;

    @Expose
    @SerializedName("en-US")
    @JsonProperty("en-US")
    private String enUS;

    @Expose
    @JsonProperty
    private long modifyDate;

    public String getCode() {
        return code;
    }

    public String getZhCN() {
        return zhCN;
    }

    public String getEnUS() {
        return enUS;
    }

    public long getModifyDate() {
        return modifyDate;
    }
}
