package com.kk.test.inject.model;

import com.kk.inject.NotNull;
import com.kk.inject.Singleton;

/**
 * Service interface.
 */
@Singleton
public interface Service {

    /**
     * Builds the greeting using the configured prefix and the user name from the login manager.
     *
     * @return The greeting. Never {@code null}.
     */
    @NotNull
    String getGreeting();
}
