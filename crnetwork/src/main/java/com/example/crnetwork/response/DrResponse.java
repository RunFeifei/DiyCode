package com.example.crnetwork.response;

import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.dataformat.Entity;
import com.example.crnetwork.dataformat.DrRoot;
import com.example.crnetwork.error.ErrorCode.DrResultCode;
import com.example.crnetwork.relogin.LoginServiceCreator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.functions.Action0;
import rx.functions.Action1;
import util.Strings;

/**
 * Created by PengFeifei on 17-4-21.
 * 只对content,result字段做了处理
 * 根数据结构
 * {
 * "content": {},
 * "result":  "...",//login or success ...
 * "message": "...",
 * "message-args": "...",
 * "errors": [ ]
 * "code": 3456
 * }
 */
//TODO T=ContentWrapper<TemplateEntityList<ErrorItem>>>
public class DrResponse<T extends Entity> {

    private static boolean logined;
    private DrResultCode drResultCode;
    private Action0 loginFailedCallBack;

    /**
     * @return 点融统一的数据格式 && 数据非空
     */
    private boolean checkRootData(Response<T> response) {
        if (response == null || !response.isSuccessful()) {
            return false;
        }
        T root = response.body();
        //是否是点融统一的数据格式
        boolean isDianrongDataFormat = root instanceof DrRoot;
        DrRoot drRoot;
        try {
            drRoot = (DrRoot) response.body();
        } catch (ClassCastException e) {
            return false;
        }
        return isDianrongDataFormat && drRoot != null;
    }

    public DrRoot getRootData(Response<T> response, final Call<T> call) {
        if (!checkRootData(response)) {
            return null;
        }
        DrRoot drRoot = (DrRoot) response.body();
        boolean result = false;
        if (drRoot != null && !Strings.isEmpty(drRoot.getResult())) {
            result = dispatchResult(drRoot, call);
        }
        return result ? drRoot : null;
    }

    public <Content extends Entity > Content getContentData(Response<T> response, final Call<T> call) {
        DrRoot drRoot = getRootData(response, call);
        if (drRoot == null) {
            return null;
        }
        Content data = (Content) drRoot.getContent();
        return data;
    }

    public <Item extends Entity> ArrayList<Item> getListData(Response<T> response, final Call<T> call) {
        DrRoot drRoot = getRootData(response, call);
        if (drRoot == null) {
            return null;
        }
        DrList drList = (DrList) drRoot.getContent();
        if (drRoot.getContent() instanceof DrList) {
            drList = (DrList) drRoot.getContent();
        }
        if (drList == null) {
            return null;
        }
        ArrayList<Item> list = drList.getList();
        return list;
    }


    /**
     * 勿在主线程执行此方法,防止死循环//TODO  clone只需要一次 防止死循环
     *
     * @param loginFailedCallBack 一般是跳转到登录页面的Callback
     */
    @SuppressWarnings("unchecked")
    private void tryLoginWithToken(final Call<T> call, Action0 loginFailedCallBack) {
        if (DrResponse.logined) {
            if (loginFailedCallBack != null) {
                loginFailedCallBack.call();
            }
            return;
        }
        LoginServiceCreator.getAutoLoginServiceFactory().create()
                .subscribe(new Action1() {
                    @Override
                    public void call(Object obj) {
                        logined = true;
                        Result<DrRoot<?>> result = (Result<DrRoot<?>>) obj;
                        if (result != null) {
                            Response<DrRoot<?>> response = result.response();
                            if (response != null) {
                                DrRoot root = response.body();
                                if (root != null && root.isSuccessful()) {
                                    ResponseHandler.getSyncResponse(call);
                                }
                            }
                        }
                    }
                });
    }


    /**
     * //TODO 只处理login和Auth?????
     * 解析Response的result字段
     *
     * @param drRoot
     * @param call
     * @return
     */
    private boolean dispatchResult(DrRoot drRoot, final Call<T> call) {
        String result = drRoot.getResult();
        this.drResultCode = getDrResultCode(result);
        if (drResultCode == DrResultCode.Login || drResultCode == DrResultCode.AuthFirst) {
            tryLoginWithToken(call, loginFailedCallBack);
            return false;
        }
        return drResultCode.equals(DrResultCode.Success);
    }

    private DrResultCode getDrResultCode(String result) {
        DrResultCode drResultCode;
        if (result == null) {
            drResultCode = DrResultCode.Unknown;
        } else if (result.equals("success")) {
            drResultCode = DrResultCode.Success;
        } else if (result.equals("error")) {
            drResultCode = DrResultCode.Error;
        } else if (result.equals("login")) {
            drResultCode = DrResultCode.Login;
        } else if (result.equals("service_disabled")) {
            drResultCode = DrResultCode.ServiceDisabled;
        } else if (result.equals("session_timeout")) {
            drResultCode = DrResultCode.SessionTimeout;
        } else if (result.equals("lender_not_a_member")) {
            drResultCode = DrResultCode.LenderNotAMember;
        } else {
            drResultCode = DrResultCode.Unknown;
        }
        return drResultCode;
    }

    public void setLoginFailedCallBack(Action0 loginFailedCallBack) {
        this.loginFailedCallBack = loginFailedCallBack;
    }


}
