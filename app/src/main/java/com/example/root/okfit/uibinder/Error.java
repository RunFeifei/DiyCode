package com.example.root.okfit.uibinder;


import com.dianrong.crnetwork.response.RequestException;

/**
 * Created by Lei Guoting on 17-3-19.
 */

public interface Error {
    boolean onError(RequestException error);
}
