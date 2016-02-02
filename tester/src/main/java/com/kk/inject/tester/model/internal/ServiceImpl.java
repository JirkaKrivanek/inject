package com.kk.inject.tester.model.internal;

import com.kk.inject.Inject;
import com.kk.inject.tester.model.Configuration;
import com.kk.inject.tester.model.DgPrefix;
import com.kk.inject.tester.model.LoginManager;
import com.kk.inject.tester.model.Service;

/**
 * Service implementation.
 */
public class ServiceImpl implements Service {

    private final Configuration mConfiguration;
    private final LoginManager mLoginManager;
    @Inject
    @DgPrefix
    private String mDgPrefix;

    /**
     * Constructs the service.
     *
     * @param loginManager
     *         The injected login manager.
     */
    @Inject
    public ServiceImpl(final Configuration configuration, final LoginManager loginManager) {
        mConfiguration = configuration;
        mLoginManager = loginManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void greetServer() {
        System.out.println(mDgPrefix + ": Hello: " + mConfiguration.getUrl() + ", this is " + mLoginManager.getUserId());
    }
}
