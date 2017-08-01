package com.example.root.okfit.util.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PengFeifei on 17-8-2.
 */

public class RequestLog {

    private static final String TAG = "OKHttp";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int JSON_INDENT = 4;
    private static final String SUFFIX = ".java";


    public static void D(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printTopLine("Response");
        message = getStackTrace() + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(TAG, "║ " + line);
        }
        printBottomLine();
    }

    private static void printTopLine(String hint) {
        printLine(TAG, true, hint);
    }

    private static void printBottomLine() {
        printLine(TAG, false, null);
    }

    private static void printLine(String tag, boolean isTop, String hint) {
        if (isTop) {
            final String fotmat = "╔════════%s═══════════════════════════════════════════════════════════════════════";
            hint = TextUtils.isEmpty(hint) ? "════════" : hint;
            String fotmated = String.format(fotmat, hint);
            Log.i(tag, fotmated);
        } else {
            Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }


    private static String getStackTrace() {
        final int STACK_TRACE_INDEX_4 = 4;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX_4];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        return "[ (" + className + ":" + lineNumber + ")#" + methodName + " ] ";
    }


}
