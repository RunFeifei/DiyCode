package com.dianrong.crnetwork.dataformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by PengFeifei on 17-7-25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public interface Entity extends Serializable {
}
