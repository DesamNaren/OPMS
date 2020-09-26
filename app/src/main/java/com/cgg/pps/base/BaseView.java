package com.cgg.pps.base;

import android.content.Context;

public interface BaseView {
    Context getContext();
    void onError(int code, String msg);
}
