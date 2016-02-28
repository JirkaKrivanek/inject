package com.kk.inject.testsdk.internal;

import com.kk.inject.Inject;
import com.kk.inject.NotNull;
import com.kk.inject.testsdk.GreetingManager;
import com.kk.inject.testsdk.User;

/**
 * User implementation.
 */
public class UserImpl implements User {

    /**
     * The clue between the greeting and the user name.
     */
    @NotNull private static final String CLUE = ", I am ";

    /**
     * The injected greeting manager.
     */
    @NotNull @Inject private GreetingManager mGreetingManager;

    /**
     * The usr name passed to the constructor.
     */
    @NotNull private String mUserName;

    /**
     * Constructs the user with the specified user name.
     *
     * @param userName
     *         The user name for the user.
     */
    public UserImpl(@NotNull final String userName) {
        mUserName = userName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String introduce() {
        return mGreetingManager.getGreeting() + CLUE + mUserName;
    }
}
