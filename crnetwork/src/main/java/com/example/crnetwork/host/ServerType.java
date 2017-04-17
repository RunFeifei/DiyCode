package com.example.crnetwork.host;

import android.support.annotation.IntDef;

/**
 * Created by PengFeifei on 17-4-19.
 */

@IntDef({ServerType.PRODUCT, ServerType.DEMO, ServerType.DEV})
public @interface ServerType {
    int PRODUCT = 0;
    int DEMO = 1;
    int DEV = 2;
}
