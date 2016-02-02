package com.kk.inject.tester.model;

import com.kk.inject.Singleton;

/**
 * Login manager interface.
 */
@Singleton
public interface LoginManager {

    /**
     * Retrieves the user ID.
     *
     * @return The user ID.
     */
    String getUserId();
}
