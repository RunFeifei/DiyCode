package com.dianrong.crnetwork.framework.view.loading;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.crnetwork.R;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

/**
 * Created by PengFeifei on 17-6-16.
 */

public class LoadingDialog extends RxDialogFragment implements View.OnClickListener {

    private static final String TAG = LoadingDialog.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_loading, container);
        setCancelable(false);
        view.findViewById(R.id.imgDismiss).setOnClickListener(this);
        return view;
    }

    public static Dialog showLoading(FragmentManager fragmentManager) {
        final FragmentManager manager=fragmentManager;
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(manager);
        return loadingDialog.getDialog();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgDismiss) {
            dismiss();
        }
    }

    public void dismiss() {
        if (isVisible()) {
            super.dismiss();
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
            //            throw new RequestException();
        }
    }
}
