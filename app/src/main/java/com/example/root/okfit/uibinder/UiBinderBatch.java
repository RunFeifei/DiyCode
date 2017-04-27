package com.example.root.okfit.uibinder;

/**
 * Created by Lei Guoting on 17-3-19.
 */
public interface UiBinderBatch {

    <T> UiBinder<T> born();

    void apply();

    void apply(int type);
}
