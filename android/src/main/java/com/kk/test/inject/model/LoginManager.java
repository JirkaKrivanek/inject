package com.kk.test.inject.model;

import com.kk.inject.NotNull;

/**
 * Login manager interface.
 */
public interface LoginManager {

    /**
     * Retrieves the name of the currently logged in user.
     *
     * @return The user name. Never {@code null}.
     */
    @NotNull
    String getUserName();
}
