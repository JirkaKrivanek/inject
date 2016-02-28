package com.kk.test.inject;

import android.app.Application;
import android.content.Context;

import com.kk.inject.testsdk.TestSdkContext;

/**
 * Android application.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TestSdkContext.getFactory().whenRequestedInstanceOf(Context.class).thenReturn(getApplicationContext());
    }
}
