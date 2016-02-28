package com.kk.inject.testsdk;

import com.kk.inject.Module;
import com.kk.inject.NotNull;
import com.kk.inject.testsdk.internal.GreetingManagerImpl;
import com.kk.inject.testsdk.internal.UserImpl;

/**
 * The greeting module.
 */
public final class GreetingModule extends Module {

    /**
     * The default greeting.
     */
    @NotNull private static final String GREETING = "Hello";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn(GREETING);
        whenRequestedInstanceOf(User.class).thenInstantiate(UserImpl.class);
        whenRequestedInstanceOf(GreetingManager.class).singleton().thenInstantiate(GreetingManagerImpl.class);
    }
}
