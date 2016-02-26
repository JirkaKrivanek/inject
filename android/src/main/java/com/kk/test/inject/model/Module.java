package com.kk.test.inject.model;

import com.kk.test.inject.model.internal.LoginManagerImpl;
import com.kk.test.inject.model.internal.ServiceImpl;

/**
 * Injection module.
 */
public class Module extends com.kk.inject.Module {

    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifNamed("greetingPrefix").thenReturn("Hello");
        whenRequestedInstanceOf(String.class).ifNamed("userName").thenReturn("World");
        whenRequestedInstanceOf(Service.class).thenInstantiate(ServiceImpl.class);
        whenRequestedInstanceOf(LoginManager.class).thenInstantiate(LoginManagerImpl.class);
    }
}
