package com.example.root.okfit.net.dianrong;

import com.fei.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by PengFeifei on 17-8-1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BreakerItem implements Entity {


    @JsonProperty
    private String code;

    @JsonProperty("zh-CN")
    private String zhCN;

    @JsonProperty("en-US")
    private String enUS;

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