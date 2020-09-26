package com.cgg.pps.base;


public interface BasePresenter<V> {

    void attachView(V view);

    void detachView();

    String handleError(Throwable e);

    void handleException(Exception e);


}

