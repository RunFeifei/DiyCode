package com.dianrong.crnetwork.dataformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by yangcheng on 15/3/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmptyEntity implements Entity {

    private static final long serialVersionUID = 1L;

}
