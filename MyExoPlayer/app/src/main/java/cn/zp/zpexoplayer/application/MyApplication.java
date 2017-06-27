package cn.zp.zpexoplayer.application;

import android.app.Application;

import cn.zp.zpexoplayer.model.TagProject;


/**
 * Created by admin on 2017/6/27.
 */

public class MyApplication extends Application {
    public static TagProject tagProject;
    private static MyApplication app;

    public synchronized static MyApplication getAppContext() {
        return app;
    }

    public synchronized static TagProject getTagProject() {
        return tagProject;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        tagProject = new TagProject();
    }
}
