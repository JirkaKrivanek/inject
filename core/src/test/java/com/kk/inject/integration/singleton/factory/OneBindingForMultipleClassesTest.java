package com.kk.inject.integration.singleton.factory;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Singleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the single binding for multiple classes.
 */
public class OneBindingForMultipleClassesTest {

    private static final String GREETING  = "Hello";
    private static final String USER_NAME = "World";

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Multiple injected classes
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface UserI {

        String greet();
    }

    private static class User implements UserI {

        @Inject @UserName private String  mUserName;
        @Inject private           Service mService;

        @Override
        public String greet() {
            return mService.getGreeting() + " " + mUserName;
        }
    }

    @Singleton
    private interface ServiceI {

        String greet();
    }

    private static class Service implements ServiceI {

        @Inject @Greeting private String mGreeting;
        @Inject private           UserI  mUser;

        public String getGreeting() {
            return mGreeting;
        }

        @Override
        public String greet() {
            return mUser.greet();
        }
    }

    public static class Module extends com.kk.inject.Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn(GREETING);
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn(USER_NAME);
            whenRequestedInstanceOf(UserI.class).thenInstantiate(User.class);
            whenRequestedInstanceOf(ServiceI.class).addForClass(Service.class).thenInstantiate(Service.class);
        }
    }

    @Test
    public void twoClassesForOneBinding() {
        Factory.addModuleClass(Module.class);
        final ServiceI service = Factory.getInstance(ServiceI.class);
        Assert.assertEquals(GREETING + " " + USER_NAME, service.greet());
    }
}
