package com.dianrong.crnetwork.error;

import com.dianrong.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class DrErrorItemBean implements Entity {

    @JsonProperty("code")
    private String code;

    @JsonProperty("zh-CN")
    private String zhCN;

    @JsonProperty("en-US")
    private String enUS;

    @JsonProperty("modifyDate")
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
