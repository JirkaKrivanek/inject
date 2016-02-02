package com.kk.inject.tester.model;

/**
 * Configuration interface.
 */
public interface Configuration {

    /**
     * Retrieves the prefix for the user ID.
     *
     * @return The prefix.
     */
    String getUserIdPrefix();

    /**
     * Retrieves the configured server URL.
     *
     * @return The server URL.
     */
    String getUrl();
}
