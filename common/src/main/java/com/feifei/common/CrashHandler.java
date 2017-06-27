package com.feifei.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import com.feifei.common.utils.ContextUtils;
import com.feifei.common.utils.Log;
import com.feifei.common.utils.PreferenceUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;

public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();
    private static final String FILE_KEY = "crash_file";
    private static final int TOAST_TIME = 2000;
    private static LinkedList<String> crashStacks;
    private static CrashHandler instance;
    private final Context context;
    private UncaughtExceptionHandler defaultHandler;

    private Handler uiHandler;

    private CrashHandler(Context c) {
        this.context = c;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static void init(Context context) throws Exception {
        if (CrashHandler.instance != null) {
            throw new Exception("The CrashHandler is already initialized.");
        }

        crashStacks = PreferenceUtil.getObject(context, FILE_KEY);
        if (crashStacks == null) {
            crashStacks = new LinkedList<>();
        }

        CrashHandler.instance = new CrashHandler(context);
    }

    public static CrashHandler getInstance() {
        if (CrashHandler.instance == null) {
            throw new RuntimeException("The CrashHandler is not initialized.");
        }
        return CrashHandler.instance;
    }

    public static List<String> getCrashLog() {
        return crashStacks;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!this.handleException(ex) && this.defaultHandler != null) {
            Log.e(CrashHandler.TAG, "defaultHandler.uncaughtException");
            // default one
            this.defaultHandler.uncaughtException(thread, ex);
        } else {
            // time left for toast
            try {
                Thread.sleep(CrashHandler.TOAST_TIME);
            } catch (InterruptedException e) {
                Log.e(CrashHandler.TAG, "Error : ", e);
            }

            System.exit(1);
        }
    }

    private boolean handleException(final Throwable ex) {
        if (BuildConfig.DEBUG) {
            Log.i(CrashHandler.TAG, "handleException");
        }
        if (ex == null) {
            if (BuildConfig.DEBUG) {
                Log.i(CrashHandler.TAG, "No exception");
            }
            return BuildConfig.DEBUG;
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                uiHandler.post(() -> Toast.makeText(context, "很抱歉，程序遭遇异常，即将退出",
                        Toast.LENGTH_SHORT).show());
                CrashHandler.this.saveCrashInfoToFile(ex, true);
                Looper.loop();
            }
        }.start();

        return BuildConfig.DEBUG;
    }

    public void saveCrashInfoToFile(Throwable ex, boolean crashed) {

        Log.e(CrashHandler.TAG, "Crash Log BEGIN" + (crashed ? "" : "(captured)"));

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        Log.e(CrashHandler.TAG, result);

        try {
            result = ContextUtils.getVersionName(MultiApplication.getContext()) + "/" + ContextUtils.getVersionCode(MultiApplication.getContext()) + "\n" + System.currentTimeMillis() + "\n" + ("CAPTURED/" + !crashed) + "\n" + result;
            if (crashStacks.size() >= 5) {
                crashStacks.removeLast();
            }
            crashStacks.addFirst(result);
            PreferenceUtil.putObject(context, FILE_KEY, crashStacks);
        } catch (Exception e) {
            Log.e(CrashHandler.TAG, "an error occured while writing report file...", e);
        }

        Log.i(CrashHandler.TAG, "Crash Log END");
    }

}
