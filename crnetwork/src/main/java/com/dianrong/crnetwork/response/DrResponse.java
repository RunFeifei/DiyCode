package com.dianrong.crnetwork.response;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.error.ErrorCode;
import com.dianrong.crnetwork.dataformat.Entity;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.error.DrErrorMsgHelper;
import com.dianrong.crnetwork.relogin.LoginServiceCreator;

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
//Note T=ContentWrapper<TemplateEntityList<ErrorItem>>>
public class DrResponse<T extends Entity> {

    private boolean logined;
    private ErrorCode.DrResultCode drResultCode;
    private static Action0 loginFailedCallBack;
    private String drErrMsg;
    private String drCode;

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

    private DrRoot getRootData(Response<T> response, final Call<T> call) {
        if (!checkRootData(response)) {
            return null;
        }
        DrRoot drRoot = (DrRoot) response.body();
        int code = drRoot.getCode();
        drCode = Integer.toString(code);
        drErrMsg = DrErrorMsgHelper.getErrorMsg(drCode);
        boolean result = false;
        if (!Strings.isEmpty(drRoot.getResult())) {
            result = dispatchResult(drRoot, call);
        }
        if (!result) {
            String errMsg=new StringBuilder("parse drReponse failed").append(drErrMsg).toString();
            throw new RequestException(call.request().url(), code, errMsg);
        }
        return drRoot;
    }

    public <Content extends Entity> Content getContentData(Response<T> response, final Call<T> call) {
        DrRoot drRoot = getRootData(response, call);
        if (drRoot == null) {
            return null;
        }
        return (Content) drRoot.getContent();
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
        return drList.getList();
    }


    /**
     * 勿在主线程执行此方法,防止死循环
     *
     * @param loginFailedCallBack 一般是跳转到登录页面的Callback
     */
    @SuppressWarnings("unchecked")
    private void tryLoginWithToken(final Call<T> call, Action0 loginFailedCallBack) {
        if (logined) {
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
     * //NOTE 只处理login和Auth
     * 解析Response的result字段
     *
     * @param drRoot
     * @param call
     * @return
     */
    private boolean dispatchResult(DrRoot drRoot, final Call<T> call) {
        String result = drRoot.getResult();
        this.drResultCode = getDrResultCode(result);
        if (drResultCode == ErrorCode.DrResultCode.Login || drResultCode == ErrorCode.DrResultCode.AuthFirst) {
            tryLoginWithToken(call, loginFailedCallBack);
            return false;
        }
        return drResultCode.equals(ErrorCode.DrResultCode.Success);
    }

    private ErrorCode.DrResultCode getDrResultCode(String result) {
        ErrorCode.DrResultCode drResultCode;
        if (result == null) {
            drResultCode = ErrorCode.DrResultCode.Unknown;
        } else if (result.equals("success")) {
            drResultCode = ErrorCode.DrResultCode.Success;
        } else if (result.equals("error")) {
            drResultCode = ErrorCode.DrResultCode.Error;
        } else if (result.equals("login")) {
            drResultCode = ErrorCode.DrResultCode.Login;
        } else if (result.equals("service_disabled")) {
            drResultCode = ErrorCode.DrResultCode.ServiceDisabled;
        } else if (result.equals("session_timeout")) {
            drResultCode = ErrorCode.DrResultCode.SessionTimeout;
        } else if (result.equals("lender_not_a_member")) {
            drResultCode = ErrorCode.DrResultCode.LenderNotAMember;
        } else {
            drResultCode = ErrorCode.DrResultCode.Unknown;
        }
        return drResultCode;
    }

    // NOTE: 17-5-5 在父类中初始化此回调
    public void setLoginFailedCallBack(Action0 loginFailedCallBack) {
        this.loginFailedCallBack = loginFailedCallBack;
    }


}
