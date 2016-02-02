package com.kk.inject.tester.main;

import com.kk.inject.Factory;
import com.kk.inject.tester.TesterModule;
import com.kk.inject.tester.model.Configuration;
import com.kk.inject.tester.model.Service;

/**
 * Main tester class.
 */
public class Main {

    /**
     * Executes the code.
     *
     * @param args
     *         The command line arguments.
     */
    public static void main(final String[] args) {
        final Configuration configuration = new Configuration() {
            @Override
            public String getUserIdPrefix() {
                return "Mr.";
            }

            @Override
            public String getUrl() {
                return "www.google.com";
            }
        };

        new TesterModule(Factory.singleton(), configuration);
        final Service service = Factory.singleton().get(Service.class);
        service.greetServer();
    }
}
