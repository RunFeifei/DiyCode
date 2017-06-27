package com.example.root.okfit.net.bean;

import com.dianrong.crnetwork.dataformat.Entity;

/**
 * Created by PengFeifei on 17-7-25.
 */

public class Abilities implements Entity {
    private boolean update;

    private boolean destroy;

    public void setUpdate(boolean update){
        this.update = update;
    }
    public boolean getUpdate(){
        return this.update;
    }
    public void setDestroy(boolean destroy){
        this.destroy = destroy;
    }
    public boolean getDestroy(){
        return this.destroy;
    }

}