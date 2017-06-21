package com.dianrong.crnetwork.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import okhttp3.HttpUrl;

/**
 * Created by PengFeifei on 17-4-21.
 * errMsg和errCode通过Exception的方式传递到UI
 */

public class RequestException extends RuntimeException implements Parcelable {

    private HttpUrl url;
    private int code;
    private String errMsg;

    public static HttpUrl REQUEST_UNKNOWN = HttpUrl.parse("http://www.unKnownAPI.com");

    public RequestException(Parcel source) {
        super((Throwable) source.readSerializable());
        this.url = HttpUrl.parse(source.readString());
        this.code = source.readInt();
        this.errMsg = source.readString();
    }


    public RequestException(HttpUrl url, int code, Throwable cause) {
        super(cause);
        this.url = url;
        this.code = code;
        this.errMsg = cause.getMessage();
    }


    public RequestException(HttpUrl url, int code, String errMsg) {
        super(new Throwable(errMsg));
        this.url = url;
        this.code = code;
        this.errMsg = errMsg;
    }

    public String getUrl() {
        return url.toString();
    }

    public int getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url != null ? url.toString() : "null");
        dest.writeInt(code);
        dest.writeString(errMsg);
    }

    public static final Parcelable.Creator<RequestException> CREATOR = new Creator<RequestException>() {
        @Override
        public RequestException createFromParcel(Parcel source) {
            return new RequestException(source);
        }

        @Override
        public RequestException[] newArray(int size) {
            return new RequestException[size];
        }
    };


}