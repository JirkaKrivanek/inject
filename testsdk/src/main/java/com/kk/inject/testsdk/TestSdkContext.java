package com.kk.inject.testsdk;

import com.kk.inject.Factory;
import com.kk.inject.NotNull;

/**
 * Context of the test SDK. This is where it all starts from.
 */
public final class TestSdkContext {

    private static boolean sFactoryDefined = false;

    /**
     * Retrieves the factory configured for this SDK.
     *
     * @return The configured factory. Never {@code null}.
     */
    @NotNull
    public static synchronized Factory getFactory() {
        if (!sFactoryDefined) {
            sFactoryDefined = true;
            Factory.addModuleClass(GreetingModule.class);
        }
        return Factory.getSingleton();
    }
}
