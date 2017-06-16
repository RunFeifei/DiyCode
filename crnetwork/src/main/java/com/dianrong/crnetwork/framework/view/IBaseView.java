package com.dianrong.crnetwork.framework.view;


import android.support.v4.app.FragmentManager;

import com.dianrong.crnetwork.response.RequestException;

/**
 * Created by PengFeifei on 17-6-13.
 */

public interface IBaseView {

    void onRequestStart();

    void onRequestEnd();

    void onRequestError(RequestException exception);

    boolean onPageVisible();

    FragmentManager onLoading();

}
