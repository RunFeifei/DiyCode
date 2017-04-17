package com.example.root.okfit.bean.breakers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class BreakContent {

    @Expose
    @SerializedName("list")
    @JsonProperty("list")
    private List<BreakerItem> list ;

    @Expose
    @SerializedName("totalRecords")
    @JsonProperty("totalRecords")
    private int totalRecords;

    public List<BreakerItem> getList(){
        return this.list;
    }
    public int getTotalRecords(){
        return this.totalRecords;
    }
}
