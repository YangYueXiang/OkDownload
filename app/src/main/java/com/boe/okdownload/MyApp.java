package com.boe.okdownload;

import android.app.Application;
import android.os.Environment;


import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;

import java.io.File;

public class MyApp extends Application {
    private static MyApp application;
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
        OkDownload okDownload = OkDownload.getInstance();
        okDownload.setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CESHI" + File.separator); //设置全局下载目录
        okDownload.getThreadPool().setCorePoolSize(1); //设置同时下载数量
        XExecutor.OnAllTaskEndListener listener = new XExecutor.OnAllTaskEndListener() {
            @Override
            public void onAllTaskEnd() {

            }
        };
        okDownload.addOnAllTaskEndListener(listener); //设置所有任务监听
    }

    public static MyApp getInstance() {
        return application;
    }
}
