package com.kk.inject.tester;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Provides;
import com.kk.inject.tester.model.Configuration;
import com.kk.inject.tester.model.DgPrefix;
import com.kk.inject.tester.model.LoginManager;
import com.kk.inject.tester.model.Service;
import com.kk.inject.tester.model.internal.LoginManagerImpl;
import com.kk.inject.tester.model.internal.ServiceImpl;

/**
 * Tester module.
 */
public class TesterModule extends AbstractModule {

    private final Configuration mConfiguration;

    /**
     * Constructs the tester module with the configuration.
     *
     * @param configuration
     *         The configuration.
     * @param factory
     *         The factory to set to the module.
     */
    public TesterModule(final Factory factory, final Configuration configuration) {
        super(factory);
        mConfiguration = configuration;
    }

    /**
     * Produces the configuration.
     *
     * @return The configuration passed to the module constructor.
     */
    @Provides
    public Configuration provideConfiguration() {
        return mConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifAnnotatedWith(DgPrefix.class).thenReturn("DG OUTPUT");
        whenRequestedInstanceOf(Service.class).thenInstantiate(ServiceImpl.class);
        whenRequestedInstanceOf(LoginManager.class).thenInstantiate(LoginManagerImpl.class);
    }
}
