package com.fei.crnetwork.framework.view.loading;

/**
 * Created by PengFeifei on 2018/9/12.
 */
public interface Loading {
    void dismissLoading();

    void showLoading();

    void setOnDismissListener(OnLoadingDismissListener onDismissListener);

}
