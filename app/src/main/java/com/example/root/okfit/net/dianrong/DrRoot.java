package com.example.root.okfit.net.dianrong;

import com.fei.crnetwork.dataformat.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by PengFeifei on 17-8-1.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrRoot<T extends Entity> implements Entity {
    private static final long serialVersionUID = 1L;
    public static final String RESULT_SUCCESS = "success";
    private String result;
    private String[] errors;
    private String message;
    @JsonProperty("message-args")
    private String messageArgs;
    private int code;
    private T content;

    public T getContent() {
        return content;
    }

    public String getResult() {
        return result;
    }

    public String[] getErrors() {
        return errors;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageArgs() {
        return messageArgs;
    }

    public boolean isSuccessful() {
        return RESULT_SUCCESS.equals(getResult());
    }
}