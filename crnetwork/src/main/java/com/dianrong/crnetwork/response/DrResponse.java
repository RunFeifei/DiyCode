package com.dianrong.crnetwork.response;

import com.dianrong.crnetwork.dataformat.DrList;
import com.dianrong.crnetwork.dataformat.DrRoot;
import com.dianrong.crnetwork.dataformat.EmptyEntity;
import com.dianrong.crnetwork.dataformat.Entity;
import com.dianrong.crnetwork.error.DrErrorMsgHelper;
import com.dianrong.crnetwork.error.ErrorCode;
import com.dianrong.crnetwork.relogin.LoginServiceCreator;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
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

    private static volatile int logined;
    private ErrorCode.DrResultCode drResultCode;
    //errMsg和errCode通过Exception的方式传递到UI
    private String drErrMsg;
    private String drCode;
    private boolean isInterceptLogin;

    public DrResponse() {
    }

    public DrResponse(boolean isInterceptLogin) {
        this.isInterceptLogin = isInterceptLogin;
    }

    /**
     * @return 点融统一的数据格式 && 数据非空
     */
    private boolean checkRootData(final Call<T> call, Response<T> response) {
        if (response == null || !response.isSuccessful()) {
            return false;
        }
        T root = response.body();
        //是否是点融统一的数据格式
        boolean isDianrongDataFormat = root instanceof DrRoot;
        DrRoot drRoot = null;
        try {
            drRoot = (DrRoot) response.body();
        } catch (ClassCastException e) {
            throwRequestException(call.request().url(), "can not cast to dainrong root data");
        }
        boolean result = isDianrongDataFormat && drRoot != null;
        if (!result) {
            throwRequestException(call.request().url(), "can not cast to dainrong root data");
        }
        return true;
    }

    /**
     *  //TODO 当不拦截登录事件 但是返回login时 此时仍会返回droot
     * @param response
     * @param call
     * @return
     */
    private DrRoot getRootData(Response<T> response, final Call<T> call) {
        checkRootData(call, response);
        DrRoot drRoot = (DrRoot) response.body();
        boolean result = false;
        if (!Strings.isEmpty(drRoot.getResult())) {
            result = dispatchResult(drRoot, call);
        }
        if (!result) {
            int code = drRoot.getCode();
            drCode = Integer.toString(code);
            drErrMsg = DrErrorMsgHelper.getErrorMsg(drCode);
            String errMsg = new StringBuilder("dr API returns failed").append(drErrMsg).toString();
            throw new RequestException(call.request().url(), code, errMsg);
        }
        return drRoot;
    }

    public <Content extends Entity> Content getContentData(Response<T> response, final Call<T> call) {
        DrRoot drRoot = getRootData(response, call);
        if (drRoot == null) {
            throwRequestException(call.request().url(), "drRoot data retuns null");
        }
        Content contentData;
        try {
            contentData = (Content) drRoot.getContent();
        } catch (ClassCastException e) {
            throw new RequestException(call.request().url(), ErrorCode.DR_CAST_ERR, e);
        }
        return contentData;
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
            throwRequestException(call.request().url(), "drList data retuns null");
        }
        ArrayList<Item> list = null;
        try {
            list = drList.getList();
        } catch (ClassCastException e) {
            throw new RequestException(call.request().url(), ErrorCode.DR_CAST_ERR, e);
        }
        return list;
    }


    /**
     * 勿在主线程执行此方法
     * TODO 防止死循环 登录成功后还需要登录DC服务器
     * 需要重新登录时候返回的数据结构
     * 重新登录失败 成功和失败返回的数据结构
     * <p>
     * A接口返回login 请求login接口B B成功再次请求A接口 B失败抛异常
     * A接口再次返回login 抛异常
     * C接口返回login 进行下一循环
     * relogin次数最多3次 抛异常后计数器归零
     */
    @SuppressWarnings("unchecked")
    private synchronized void tryLoginWithToken(final Call<T> call) {
        if (DrResponse.logined > 2) {
            DrResponse.logined = 0;
            throw new RequestException(call.request().url(), ErrorCode.DR_RELOGIN_ERR, "already did relogin");
        }

        DrResponse.logined++;
        LoginServiceCreator.getAutoLoginServiceFactory().create()
                .subscribe(new Subscriber<Result<DrRoot<EmptyEntity>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        DrResponse.logined = 0;
                        throw new RequestException(call.request().url(), ErrorCode.DR_RELOGIN_ERR, e);
                    }

                    @Override
                    public void onNext(Result<DrRoot<EmptyEntity>> result) {
                        if (result == null) {
                            DrResponse.logined = 0;
                            throw new RequestException(call.request().url(), ErrorCode.DR_RELOGIN_ERR, "auto relogin failed");
                        }
                        Response<DrRoot<EmptyEntity>> response = result.response();
                        boolean sucess = response.isSuccessful()
                                && getRootData((Response<T>) response, call).isSuccessful();
                        if (!sucess) {
                            DrResponse.logined = 0;
                            throw new RequestException(call.request().url(), ErrorCode.DR_RELOGIN_ERR, "auto relogin failed");
                        }
                        ResponseHandler.getSyncResponse(call);
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
            if (isInterceptLogin) {
                String errMsg = new StringBuilder("USER INTERCEPTION LOGIN ERR")
                        .append(DrErrorMsgHelper.getErrorMsg(Integer.toString(drRoot.getCode())))
                        .toString();
                throw new RequestException(call.request().url(), ErrorCode.DR_INTERCEPTION_LOGIN_ERR, errMsg);
            }
            tryLoginWithToken(call);
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

    private void throwRequestException(HttpUrl url, String cause) {
        throw new RequestException(url, ErrorCode.DR_CAST_ERR, cause);
    }


}
