package com.example.root.okfit.net.dianrong;

import com.dianrong.crnetwork.dataformat.AList;
import com.dianrong.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by PengFeifei on 17-8-1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrList<T extends Entity> implements Entity {
    private static final long serialVersionUID = 1L;
    private AList<T> list;
    private long totalRecords;

    public long getTotalRecords() {
        return totalRecords;
    }

    public AList<T> getList() {
        return list;
    }
}