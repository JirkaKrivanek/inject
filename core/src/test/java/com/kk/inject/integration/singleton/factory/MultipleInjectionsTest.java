package com.kk.inject.integration.singleton.factory;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Module;
import com.kk.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the multiple injection.
 */
public class MultipleInjectionsTest {

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor injection
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserCI {

        String getUserName();
    }

    private interface IGreeterCI {

        void greet();
    }

    private static class UserCI implements IUserCI {

        private final String mUserName;

        @Inject
        public UserCI(final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterCI implements IGreeterCI {

        private       String  mGreeting;
        private final IUserCI mUser;

        @Inject
        public GreeterCI(final IUserCI user) {
            mUser = user;
            mGreeting = "";
        }

        @Override
        public void greet() {
            mGreeting = "Hello " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleCI extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).thenReturn("World");
            whenRequestedInstanceOf(IUserCI.class).thenInstantiate(UserCI.class);
            whenRequestedInstanceOf(IGreeterCI.class).thenInstantiate(GreeterCI.class);
        }
    }

    @Test
    public void injectCI() {
        Factory.addModuleClass(ModuleCI.class);
        final IGreeterCI greeter = Factory.getInstance(IGreeterCI.class);
        Assert.assertEquals("", ((GreeterCI) greeter).mGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterCI) greeter).mGreeting);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Field injection
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserFI {

        String getUserName();
    }

    private interface IGreeterFI {

        void greet();
    }

    private static class UserFI implements IUserFI {

        @Inject private String mUserName;

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterFI implements IGreeterFI {

        @Inject private IUserFI mUser;
        private         String  mGreeting;

        public GreeterFI() {
            mGreeting = "";
        }

        @Override
        public void greet() {
            mGreeting = "Hello " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleFI extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).thenReturn("World");
            whenRequestedInstanceOf(IUserFI.class).thenInstantiate(UserFI.class);
            whenRequestedInstanceOf(IGreeterFI.class).thenInstantiate(GreeterFI.class);
        }
    }

    @Test
    public void injectFI() {
        Factory.addModuleClass(ModuleFI.class);
        final IGreeterFI greeter = Factory.getInstance(IGreeterFI.class);
        Assert.assertEquals("", ((GreeterFI) greeter).mGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterFI) greeter).mGreeting);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Method injection
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserMI {

        String getUserName();
    }

    private interface IGreeterMI {

        void greet();
    }

    private static class UserMI implements IUserMI {

        private String mUserName;

        @Inject
        public void setUserName(@Named("userName") final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterMI implements IGreeterMI {

        private String  mSaidGreeting;
        private IUserMI mUser;
        private String  mGreeting;

        public GreeterMI() {
            mSaidGreeting = "";
        }

        @Inject
        public void setup(@Named("greeting") final String greeting, final IUserMI user) {
            mGreeting = greeting;
            mUser = user;
        }

        @Override
        public void greet() {
            mSaidGreeting = mGreeting + " " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleMI extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifNamed("userName").thenReturn("World");
            whenRequestedInstanceOf(String.class).ifNamed("greeting").thenReturn("Hello");
            whenRequestedInstanceOf(IUserMI.class).thenInstantiate(UserMI.class);
            whenRequestedInstanceOf(IGreeterMI.class).thenInstantiate(GreeterMI.class);
        }
    }

    @Test
    public void injectMI() {
        Factory.addModuleClass(ModuleMI.class);
        final IGreeterMI greeter = Factory.getInstance(IGreeterMI.class);
        Assert.assertEquals("", ((GreeterMI) greeter).mSaidGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterMI) greeter).mSaidGreeting);
    }
}
