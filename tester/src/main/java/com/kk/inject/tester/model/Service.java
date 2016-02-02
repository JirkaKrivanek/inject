package com.kk.inject.tester.model;

import com.kk.inject.Singleton;

/**
 * Service interface.
 */
@Singleton
public interface Service {

    /**
     * Says hello to the server.
     */
    void greetServer();
}
