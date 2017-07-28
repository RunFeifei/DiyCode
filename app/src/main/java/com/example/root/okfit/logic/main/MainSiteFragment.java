package com.example.root.okfit.logic.main;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dianrong.crnetwork.dataformat.AList;
import com.dianrong.crnetwork.framework.RequestAgent;
import com.dianrong.crnetwork.framework.subscriber.DefaultSubscriber;
import com.dianrong.crnetwork.request.RequestHandler;
import com.example.root.okfit.R;
import com.example.root.okfit.base.BaseWebviewActivity;
import com.example.root.okfit.net.api.SitesApi;
import com.example.root.okfit.net.bean.Sites;
import com.fei.root.recater.adapter.multi.ItemModule;
import com.fei.root.recater.adapter.multi.MultiAdapter;
import com.fei.root.recater.viewholder.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainSiteFragment extends MainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<ItemModule> listDatas;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        listDatas = new ArrayList<>();
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return listDatas.get(position) instanceof Sites ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        requestData();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.frament_main_sites;
    }


    private void requestData() {
        RequestAgent<AList<Sites>> requestAgent = new RequestAgent<>(this);
        requestAgent
                .bindObservable(RequestHandler.getService(SitesApi.class).getSites())
                .onObservable()
                .subscribe(new DefaultSubscriber<AList<Sites>>(this) {
                    @Override
                    public void onHandleData(AList<Sites> sites) {
                        initDatas(sites);
                    }
                })
        ;
    }

    private void initDatas(AList<Sites> list) {
        listDatas.clear();
        for (Sites sites : list) {
            listDatas.add(sites);
            for (Sites.Site site : sites.getSites()) {
                listDatas.add(site);
            }
        }
        MultiAdapter<ItemModule> adapter = new MultiAdapter<ItemModule>(listDatas) {
            @Override
            protected void convert(CommonHolder holder, ItemModule module, int position) {
                if (module instanceof Sites) {
                    Sites sites = (Sites) module;
                    holder.setText(R.id.sites, sites.getName());
                    return;
                }
                if (module instanceof Sites.Site) {
                    Sites.Site site = (Sites.Site) module;
                    ImageView imageView = holder.getImageView(R.id.logo);
                    Glide.with(getContext()).load(site.getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into(imageView);
                    holder.setText(R.id.site, site.getName());
                    holder.itemView.setOnClickListener((v)-> BaseWebviewActivity.openLink(getContext(),site.getUrl()));
                }
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
