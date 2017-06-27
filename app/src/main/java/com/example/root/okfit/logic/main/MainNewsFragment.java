package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.root.okfit.R;
import com.fei.root.recater.adapter.RefloadAdapter;
import com.fei.root.recater.listener.AdapterListeners;
import com.fei.root.recater.view.DefaultRefreshFooterView;
import com.fei.root.recater.view.DefaultRefreshHeaderView;
import com.fei.root.recater.view.RefloadRecyclerView;
import com.fei.root.recater.viewholder.CommonHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainNewsFragment extends MainFragment {

    @BindView(R.id.recyclerView)
    RefloadRecyclerView recyclerView;

    @Override
    protected void init(Bundle savedInstanceState, View view) {

        int i=78787878;
        ArrayList<String> list = new ArrayList<>();
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        list.add(i++ + "");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        RefloadAdapter<String> commonAdapter = new RefloadAdapter<String>(list, R.layout.item_topics_recycler_view) {
            @Override
            protected void convert(CommonHolder holder, String s, int position) {
                holder.setText(R.id.title,"deded");
            }
        };
        recyclerView.setAdapter(commonAdapter);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.frament_main_news;
    }
}
