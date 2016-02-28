package com.kk.inject.testsdk.internal;

import android.content.Context;

import com.kk.inject.Inject;
import com.kk.inject.NotNull;
import com.kk.inject.testsdk.GreetingManager;
import com.kk.inject.testsdk.R;

/**
 * Implementation of the greeting manager.
 */
public class GreetingManagerImpl implements GreetingManager {

    /**
     * The Android Application Context to retrieve the localized greeting from.
     */
    @NotNull @Inject private Context mAndroidApplicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getGreeting() {
        return mAndroidApplicationContext.getResources().getString(R.string.greeting);
    }
}
