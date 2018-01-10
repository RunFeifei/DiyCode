package com.example.root.okfit.net.bean;

import com.fei.crnetwork.dataformat.Entity;
import com.example.root.okfit.R;
import com.fei.root.recater.adapter.multi.ItemModule;

import java.util.List;

public class Sites implements Entity, ItemModule {
    private String name;
    private int id;
    private List<Site> sites;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Site> getSites() {
        return sites;
    }

    @Override
    public int itemViewLayoutId() {
        return R.layout.item_sites;
    }

    public static class Site implements Entity, ItemModule {

        private String name;
        private String url;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        @Override
        public int itemViewLayoutId() {
            return R.layout.item_site;
        }
    }
}
