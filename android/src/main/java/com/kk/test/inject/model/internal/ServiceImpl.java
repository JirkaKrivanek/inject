package com.kk.test.inject.model.internal;

import com.kk.inject.Inject;
import com.kk.inject.Named;
import com.kk.test.inject.model.LoginManager;
import com.kk.test.inject.model.Service;

/**
 * Service implementation.
 */
public class ServiceImpl implements Service {
    @Inject
    private LoginManager mLoginManager;
    @Inject
    @Named("greetingPrefix")
    private String mPrefix;

    @Override
    public String getGreeting() {
        return mPrefix + " " + mLoginManager.getUserName() + "!";
    }
}
