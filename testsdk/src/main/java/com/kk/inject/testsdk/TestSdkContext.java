package com.kk.inject.testsdk;

import com.kk.inject.Factory;

/**
 * Context of the test SDK. This is where it all starts from.
 */
public final class TestSdkContext {

    /**
     * Retrieves the factory configured for this SDK.
     *
     * @return The configured factory.
     */
    public static Factory getFactory() {
        return Factory.getSingleton();
    }
}
