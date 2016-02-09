package com.kk.test.inject.model.internal;

import com.kk.inject.Inject;
import com.kk.inject.Named;
import com.kk.test.inject.model.LoginManager;
import com.kk.test.inject.model.Service;

/**
 * Login manager implementation.
 */
public class LoginManagerImpl implements LoginManager {
    @Inject
    private Service mService;
    @Inject
    @Named("userName")
    private String mUserName;

    @Override
    public String getUserName() {
        return mUserName;
    }
}
