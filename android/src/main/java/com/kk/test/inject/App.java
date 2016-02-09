package com.kk.test.inject;

import android.app.Application;
import com.kk.inject.Factory;
import com.kk.test.inject.model.Module;

/**
 * Android application.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Factory.registerModule(Module.class);
    }
}
