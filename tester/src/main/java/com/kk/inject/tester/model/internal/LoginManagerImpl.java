package com.kk.inject.tester.model.internal;

import com.kk.inject.Inject;
import com.kk.inject.tester.model.Configuration;
import com.kk.inject.tester.model.LoginManager;

/**
 * Login manager implementation.
 */
public class LoginManagerImpl implements LoginManager {
    private Configuration mConfiguration;
    @Inject
    private ServiceImpl mService;

    @Inject
    public void setConfiguration(final Configuration configuration) {
        mConfiguration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return mConfiguration.getUserIdPrefix() + " " + "John Doe";
    }
}
