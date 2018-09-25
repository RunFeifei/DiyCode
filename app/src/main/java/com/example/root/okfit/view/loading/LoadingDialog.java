package com.example.root.okfit.view.loading;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.root.okfit.R;
import com.fei.crnetwork.framework.view.loading.Loading;
import com.fei.crnetwork.framework.view.loading.OnLoadingDismissListener;

/**
 * Created by PengFeifei on 17-6-16.
 */

public class LoadingDialog extends DialogFragment implements View.OnClickListener, Loading {

    private OnLoadingDismissListener onDismissListener;
    private FragmentManager fragmentManager;

    @SuppressLint({"ValidFragment"})
    public LoadingDialog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public LoadingDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(com.example.root.okfit.R.layout.dialog_loading, container);
        setCancelable(false);
        view.findViewById(com.example.root.okfit.R.id.imgDismiss).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgDismiss) {
            if (onDismissListener != null) {
                onDismissListener.onDialogCancelled();
            }
            dismiss();
        }
    }


    @Override
    public void dismissLoading() {
        dismissAllowingStateLoss();
    }

    @Override
    public void showLoading() {
        show(fragmentManager, null);
    }

    @Override
    public void setOnDismissListener(OnLoadingDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
