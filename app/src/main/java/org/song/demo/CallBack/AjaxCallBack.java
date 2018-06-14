package org.song.demo.CallBack;

/**
 * 结果回调接口
 */

public interface AjaxCallBack {

    void onSucceed(String json);


    void onFailed(Throwable throwable, int errorCode, String errorMsg);


    void onCancle();








}
