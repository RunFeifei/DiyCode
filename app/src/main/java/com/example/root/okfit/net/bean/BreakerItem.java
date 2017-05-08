package com.example.root.okfit.net.bean;

import com.dianrong.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by PengFeifei on 17-4-19.
 * 直接继承Conent,或者实现Entity都可以实现解析
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreakerItem implements Entity {

    private String app1;

    private String name;

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
