package com.example.crnetwork.dataformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Lei Guoting on 17-3-18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrList<T extends Entity> implements Entity {

    private static final long serialVersionUID = 1L;

    private ArrayList<T> list;

    private long totalRecords;

    public long getTotalRecords() {
        return totalRecords;
    }

    public ArrayList<T> getList() {
        return list;
    }
}