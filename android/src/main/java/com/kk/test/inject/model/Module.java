package com.kk.test.inject.model;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.NotNull;
import com.kk.test.inject.model.internal.LoginManagerImpl;
import com.kk.test.inject.model.internal.ServiceImpl;

/**
 * Injection module.
 */
public class Module extends AbstractModule {

    public Module(@NotNull final Factory factory) {
        super(factory);
    }

    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifNamed("greetingPrefix").thenReturn("Hello");
        whenRequestedInstanceOf(String.class).ifNamed("userName").thenReturn("World");
        whenRequestedInstanceOf(Service.class).thenInstantiate(ServiceImpl.class);
        whenRequestedInstanceOf(LoginManager.class).thenInstantiate(LoginManagerImpl.class);
    }
}
