package com.kk.inject.testsdk;

import com.kk.inject.NotNull;

/**
 * Defines the interface of the user.
 */
public interface User {

    /**
     * Asks user to introduce.
     * <p/>
     * The introduction consists of the greeting and the user name.
     *
     * @return The greeting. Never {@code null}.
     */
    @NotNull
    String introduce();
}
