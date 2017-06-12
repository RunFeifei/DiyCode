package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.root.okfit.CrRxbus.CrBusEvent;
import com.example.root.okfit.CrRxbus.CrObservable;
import com.example.root.okfit.R;
import com.example.root.okfit.logic.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainDetectiveFragment extends MainFragment {

    @BindView(R.id.text)
    TextView text;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        text.setText("ToToolPage");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frament_main_detective;
    }

    @OnClick(R.id.text)
    protected void toBubble() {
        CrObservable.getInstance().sendStickyEvent(new CrBusEvent(CrBusEvent.EventId.EVENT_MAINPAGE_SWITCH, MainActivity.TOOL));
    }

}
