package com.example.crnetwork.dataformat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Created by yangcheng on 16/3/28.
 * 定义返回数据的根数据结构
 {
 "content": {},
 "result":  "...",//login or success ...
 "message": "...",
 "message-args": "...",
 "errors": [ ]
 "code": 234
 }
 */

/**
 * 忽略掉从response json中获得的多余的字段
 * JsonIgnoreProperties(ignoreUnknown = true)
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
