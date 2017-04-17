package com.example.crnetwork.error;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.utils.Log;
import com.dianrong.android.common.utils.UserStorageUtils;
import com.example.crnetwork.RequestHandler;
import com.example.crnetwork.dataformat.DrList;
import com.example.crnetwork.dataformat.DrRoot;
import com.example.crnetwork.response.DrResponse;
import com.example.crnetwork.response.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import util.Collections;

/**
 * Created by PengFeifei on 17-4-24.
 */

public class DrErrorMsgHelper {
    private static final String TAG = "ErrorMsgHelper";
    private static final String MESSAGE_FILE_PATH = "errorMessageFile";
    private static HashMap<String, String> errMsgMap = new HashMap<>(1024);

    /**
     * 每次切换环境都需要重新拉一次!!!
     * //TODO 根据环境进行二次更新
     */
    public static void initErrorMsgs() {
        new Thread() {
            @Override
            public void run() {
                try {
                    long modifyDate = UserStorageUtils.getDRPreferences().getLong("errorMessageLastModifyDate", 0L);
                    DrResponse<DrRoot<DrList<DrErrorItemBean>>> drResponse = new DrResponse<>();
                    Call<DrRoot<DrList<DrErrorItemBean>>> call = RequestHandler.getService(DrErrorMsgRequest.class).getErrorMsg(modifyDate);
                    ArrayList<DrErrorItemBean> list = drResponse.getListData(ResponseHandler.getSyncResponse(call), call);
                    if (Collections.isEmpty(list)) {
                        return;
                    }
                    errMsgMap.clear();
                    for (DrErrorItemBean bean : list) {
                        errMsgMap.put(bean.getCode(), bean.getZhCN());
                    }
                    if (Collections.isEmpty(errMsgMap)) {
                        return;
                    }
                    UserStorageUtils.putObject(AppContext.getInstance(), MESSAGE_FILE_PATH, errMsgMap);
                } catch (Exception e) {
                    Log.logStackTrace(TAG + ": get feapi/errors failed", e);
                }
            }
        }.start();
    }

    public static String getErrorMsg(String code) {
        if (!Collections.isEmpty(errMsgMap)) {
            return errMsgMap.get(code);
        }
        errMsgMap = (HashMap<String, String>) UserStorageUtils.getObject(AppContext.getInstance(), MESSAGE_FILE_PATH);
        if (Collections.isEmpty(errMsgMap)) {
            return null;
        }
        return errMsgMap.get(code);
    }

}
