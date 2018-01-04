package com.example.root.okfit.logic.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.root.okfit.R;
import com.example.root.okfit.net.bean.UserDetail;
import com.example.root.okfit.util.TokenHelper;
import com.example.root.okfit.util.UserManager;
import com.fei.root.recater.adapter.multi.ItemModule;
import com.fei.root.recater.adapter.multi.ItemWrapper;
import com.fei.root.recater.adapter.multi.MultiAdapter;
import com.fei.root.recater.decoration.DividerDecoration;
import com.fei.root.recater.listener.AdapterListeners;
import com.fei.root.recater.viewholder.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-5-11.
 */

public final class MainMineFragment extends MainFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<ItemModule> listDatas;
    private MultiAdapter<ItemModule> adapter;

    @Override
    protected void init(Bundle savedInstanceState, View view) {
        listDatas = new ArrayList<>();
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerDecoration(Color.parseColor("#fffafafa"), 20));
        adapter = new MultiAdapter<ItemModule>(listDatas) {
            @Override
            protected void convert(CommonHolder holder, ItemModule module, int position) {
                if (module instanceof UserDetail) {
                    UserDetail item = (UserDetail) module;

                    ImageView imageView = holder.getImageView(R.id.head);
                    Glide.with(getContext())
                            .load(item.getAvatar_url()).dontAnimate().placeholder(imageView.getDrawable()).into(imageView);
                    holder.setText(R.id.name, item.getName());
                    TextView textEmail = (TextView) holder.getView(R.id.email);
                    textEmail.setText(item.getEmail());
                    textEmail.setVisibility(TextUtils.isEmpty(item.getEmail()) ? View.GONE : View.VISIBLE);
                    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "iconfont.ttf");
                    TextView iconText = (TextView) holder.getView(R.id.icon);
                    iconText.setTypeface(typeface);
                    iconText.setText(R.string.my_right);
                    return;
                }
                if (module instanceof ItemWrapper) {

                    holder.setText(R.id.count, ((ItemWrapper) module).getContent().toString());

                    TextView iconText = (TextView) holder.getView(R.id.logo);
                    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "iconfont.ttf");
                    iconText.setTypeface(typeface);

                    if (position == 1) {
                        holder.setText(R.id.hint, "我的帖子");
                        iconText.setText(R.string.my_topic);
                        return;
                    }
                    if (position == 2) {
                        holder.setText(R.id.hint, "我的评论");
                        iconText.setText(R.string.my_reply);
                        return;
                    }
                    if (position == 3) {
                        holder.setText(R.id.hint, "我的收藏");
                        iconText.setText(R.string.my_favorite);
                        return;
                    }
                    if (position == 4) {
                        holder.setText(R.id.hint, "退出登录");
                        iconText.setText(R.string.my_logout);
                    }
                }
            }
        };
        adapter.setOnItemClick(new AdapterListeners.OnItemClick<ItemModule>() {
            @Override
            public void onItemClick(ItemModule itemModule, View view, int i) {
                if (i != 4) {
                    toast("todo");
                    return;
                }
                new TokenHelper(getContext()).clearToken();
                UserManager.isLogined = false;
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().onBackPressed();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.frament_main_sites;
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        requestData();
    }

    private void requestData() {
        UserManager.getUserDetail(this, userDetail -> {
            boolean isUpdated = listDatas.size() > 0;
            Object obj = isUpdated ? listDatas.set(0, userDetail) : listDatas.add(userDetail);

            ItemWrapper<String> itemWrapper = new ItemWrapper<String>(userDetail.getTopics_count() + "", R.layout.item_mine);
            obj = isUpdated ? listDatas.set(1, itemWrapper) : listDatas.add(itemWrapper);

            itemWrapper = new ItemWrapper<String>(userDetail.getReplies_count() + "", R.layout.item_mine);
            obj = isUpdated ? listDatas.set(2, itemWrapper) : listDatas.add(itemWrapper);

            itemWrapper = new ItemWrapper<String>(userDetail.getFavorites_count() + "", R.layout.item_mine);
            obj = isUpdated ? listDatas.set(3, itemWrapper) : listDatas.add(itemWrapper);

            itemWrapper = new ItemWrapper<String>("", R.layout.item_mine);
            obj = isUpdated ? listDatas.set(4, itemWrapper) : listDatas.add(itemWrapper);
            obj = null;
            adapter.notifyDataSetChanged();
        }, null);
    }
}
