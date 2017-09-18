package com.selaliadobor.githubeventbrowser;

import android.app.Application;

import com.facebook.soloader.SoLoader;

public class GithubEventBrowserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Initializes Litho
        SoLoader.init(this, false);
    }
}
