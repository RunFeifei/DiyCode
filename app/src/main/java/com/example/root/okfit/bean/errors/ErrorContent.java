package com.example.root.okfit.bean.errors;

import com.example.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PengFeifei on 17-4-17.
 */

public class ErrorContent implements Entity {

    @Expose
    @SerializedName("list")
    @JsonProperty("list")
    private List<ErrorItem> list;

    @Expose
    private int totalRecords;

    public List<ErrorItem> getListdata() {
        return this.list;
    }

    public int getTotalRecords() {
        return this.totalRecords;
    }


}