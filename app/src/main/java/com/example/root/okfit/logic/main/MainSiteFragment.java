package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.root.okfit.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainSiteFragment extends MainFragment {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.textContent)
    TextView textContent;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        text.setText("ToMap");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.frament_main_tool;
    }

    @OnClick(R.id.text)
    protected void toBubble() {

    }
}
