package com.example.root.okfit.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.root.okfit.R;
import com.example.root.okfit.uibinder.UiBinderView;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by PengFeifei on 17-6-16.
 */

public class LoadingDialog extends RxDialogFragment {

    private static final String TAG = LoadingDialog.class.getSimpleName();
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_loading, container);
        unbinder = ButterKnife.bind(this, view);
        setCancelable(false);
        return view;
    }

    @OnClick(R.id.imgDismiss)
    protected void dismiss(View view) {
        dismiss();
    }

    public void dismiss() {
        if (isVisible()) {
            super.dismiss();
            if (unbinder != null) {
                unbinder.unbind();
            }
        }
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
        new TimerAsyncs().execute();
    }


    private class TimerAsyncs extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                //do nothing
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dismiss();
        }
    }
}
