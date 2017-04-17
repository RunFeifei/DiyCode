package com.example.root.okfit.bean.breakers;

import com.example.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by PengFeifei on 17-4-19.
 * 直接继承Conent,或者实现Entity都可以实现解析
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreakerItem implements Entity {

    /* @Expose
     @SerializedName("app")
     @JsonProperty("app")*/
    private String app1;

    /* @Expose
     @SerializedName("name")
     @JsonProperty("name")*/
    private String name;

    /* @Expose
     @SerializedName("value")
     @JsonProperty("value")*/
    private boolean value;

    public String getApp() {
        return this.app1;
    }

    public String getName() {
        return this.name;
    }

    public boolean getValue() {
        return this.value;
    }
}
