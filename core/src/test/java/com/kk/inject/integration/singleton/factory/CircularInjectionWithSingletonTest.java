package com.kk.inject.integration.singleton.factory;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Module;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the multiple injection.
 */
public class CircularInjectionWithSingletonTest {

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Circular injection with singleton
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserCIS {

        String getUserName();
    }

    private interface IGreeterCIS {

        String getUserName();

        void greet();
    }

    private static class UserCIS implements IUserCIS {

        @Inject private IGreeterCIS mGreeter;

        @Override
        public String getUserName() {
            return mGreeter.getUserName();
        }
    }

    private static class GreeterCIS implements IGreeterCIS {

        @Inject private IUserCIS mUser;
        @Inject private String   mUserName;
        private         String   mGreeting;

        public GreeterCIS() {
            mGreeting = "";
        }

        @Override
        public String getUserName() {
            return mUserName;
        }

        @Override
        public void greet() {
            mGreeting = "Hello " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleCIS extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).thenReturn("World");
            whenRequestedInstanceOf(IUserCIS.class).thenInstantiate(UserCIS.class);
            whenRequestedInstanceOf(IGreeterCIS.class).singleton().thenInstantiate(GreeterCIS.class);
        }
    }

    @Test
    public void circularInjection() {
        Factory.addModuleClass(ModuleCIS.class);
        final IGreeterCIS greeter = Factory.getInstance(IGreeterCIS.class);
        Assert.assertEquals("", ((GreeterCIS) greeter).mGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterCIS) greeter).mGreeting);
    }
}
