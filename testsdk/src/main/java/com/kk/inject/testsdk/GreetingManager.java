package com.kk.inject.testsdk;

import com.kk.inject.NotNull;

/**
 * Defines the interface of the greeting manager.
 */
public interface GreetingManager {

    /**
     * Retrieves the greeting.
     *
     * @return The greeting. Never {@code null}.
     */
    @NotNull
    String getGreeting();
}
