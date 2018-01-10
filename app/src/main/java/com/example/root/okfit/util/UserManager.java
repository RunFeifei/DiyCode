package com.example.root.okfit.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fei.crnetwork.framework.RequestAgent;
import com.fei.crnetwork.framework.error.ErrorHandler;
import com.fei.crnetwork.framework.subscriber.DefaultSubscriber;
import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.request.ClientBuilder;
import com.fei.crnetwork.request.RequestHandler;
import com.example.root.okfit.DiyCodeApp;
import com.example.root.okfit.logic.LoginActivity;
import com.example.root.okfit.net.api.LoginApi;
import com.example.root.okfit.net.api.UserApi;
import com.example.root.okfit.net.bean.Token;
import com.example.root.okfit.net.bean.UserDetail;

;

/**
 * Created by PengFeifei on 17-7-28.
 */

public class UserManager {

    public static boolean isLogined;
    public static TokenHelper tokenHelper;
    public static UserDetail userDetail;

    private static final String ClientID = "cbadb58e";
    private static final String CLIENT_SECRET = "28f26387f2cfb0fc042d2fd4b8bc9a20b9bea5d8ac75b76979fa84b7332cfc93";

    private static final String NAME = "1027615632@qq.com";
    private static final String PASSWORD = "15954167628";

    public static final String GRANT_TYPE_LOGIN = "password";             // 密码
    public static final String GRANT_TYPE_REFRESH = "refresh_token";      // 刷新令牌
    public static final String KEY_TOKEN = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    /**************************************Login****************************************/

    public static void login(IBaseView view, @NonNull String name, @NonNull String password, @NonNull OnLogin onLogin, ErrorHandler errorHandler) {
        RequestAgent<Token> requestAgent = new RequestAgent<Token>(view);
        requestAgent
                .bindObservable(RequestHandler.getService(LoginApi.class).login(ClientID, CLIENT_SECRET, GRANT_TYPE_LOGIN, name, password))
                .onObservable()
                .subscribe(new DefaultSubscriber<Token>(view, errorHandler) {
                    @Override
                    public void onHandleData(Token token) {
                        onLogin.onGetoken(token);
                    }
                })
        ;
    }

    public static void login(IBaseView view) {
        RequestAgent<Token> requestAgent = new RequestAgent<Token>(view);
        requestAgent
                .bindObservable(RequestHandler.getService(LoginApi.class).login(ClientID, CLIENT_SECRET, GRANT_TYPE_LOGIN, NAME, PASSWORD))
                .onObservable()
                .subscribe(new DefaultSubscriber<Token>(view) {
                    @Override
                    public void onHandleData(Token token) {
                        saveToken(token);
                        isLogined = true;
                        if (view instanceof LoginActivity) {
                            ((LoginActivity) view).finish();
                        }
                    }
                })
        ;
    }

    public interface OnLogin {
        void onGetoken(Token token);
    }

    /**************************************User****************************************/

    public static void getUserDetail(IBaseView view, @NonNull GetUserDetai getUserDetai, ErrorHandler errorHandler) {
        RequestAgent<UserDetail> requestAgent = new RequestAgent<UserDetail>(view);
        requestAgent
                .bindObservable(RequestHandler.getService(UserApi.class).getMe())
                .onObservable()
                .subscribe(new DefaultSubscriber<UserDetail>(view, errorHandler) {
                    @Override
                    public void onHandleData(UserDetail userDetail) {
                        UserManager.userDetail = userDetail;
                        getUserDetai.onGetUser(userDetail);
                    }
                })
        ;
    }

    public interface GetUserDetai {
        void onGetUser(UserDetail userDetail);
    }

    /**************************************Util****************************************/

    private static void saveToken(Token token) {
        tokenHelper = new TokenHelper(DiyCodeApp.getContext());
        tokenHelper.saveToken(token);
        syncToken();
    }

    private static void syncToken() {
        Token token = getToken();
        if (token == null) {
            return;
        }
        String accessToken =token.getAccess_token();
        if (TextUtils.isEmpty(accessToken)) {
            return;
        }
        accessToken = TOKEN_PREFIX + accessToken;
        ClientBuilder.putHeader(KEY_TOKEN, accessToken);
    }

    public static Token getToken() {
        if (tokenHelper == null) {
            return null;
        }
        return tokenHelper.getToken();
    }


}
