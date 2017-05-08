package com.dianrong.crnetwork.dataformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <p/>
 * Created by yangcheng on 16/4/8.
 * 实现了此接口 在bean中不需要(例如@SerializedName @JsonProperty)字段的注解???
 * 并且在bean中少写返回的字段,增加未返回的字段都不会报错??
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Entity extends Serializable {
}
