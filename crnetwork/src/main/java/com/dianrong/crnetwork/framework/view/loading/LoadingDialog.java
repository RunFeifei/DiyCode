package com.dianrong.crnetwork.framework.view.loading;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.crnetwork.BuildConfig;
import com.example.crnetwork.R;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

/**
 * Created by PengFeifei on 17-6-16.
 */

public class LoadingDialog extends RxDialogFragment implements View.OnClickListener {

    private static final String TAG = LoadingDialog.class.getSimpleName();
    public static final int TOTAL_TIME = BuildConfig.DEBUG ? 1 * 60 * 1000 : 10 * 1000;

    private OnDismissListener onDismissListener;
    private CountDownTimer timer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_loading, container);
        setCancelable(false);
        view.findViewById(R.id.imgDismiss).setOnClickListener(this);
        return view;
    }

    public static LoadingDialog showLoading(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        final FragmentManager manager = fragmentManager;
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(manager);
        return loadingDialog;
    }

    private void initTimer() {
        timer = new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Okhttp", "reuqest take " + (10 * 1000 - (int) millisUntilFinished) + " millis");
            }

            @Override
            public void onFinish() {
                if (onDismissListener != null) {
                    onDismissListener.onTimeOutDismiss();
                    onDismissListener = null;
                }
                dismiss();
            }
        };
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgDismiss) {
            if (onDismissListener != null) {
                onDismissListener.onCancell();
                onDismissListener = null;
            }
            dismiss();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
            onDismissListener = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
        if (timer != null) {
            timer.start();
        } else {
            initTimer();
            timer.start();
        }
    }


    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();

        void onCancell();

        void onTimeOutDismiss();
    }

}
