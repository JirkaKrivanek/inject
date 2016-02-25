package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the named injections.
 */
public final class AnnotatedInjectionsTest {

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Named injections in constructor
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserC {

        String getUserName();
    }

    private interface IGreeterC {

        void greet();
    }

    private static class UserC implements IUserC {

        private final String mUserName;

        @Inject
        public UserC(@UserName final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterC implements IGreeterC {

        private       String mSaidGreeting;
        private final String mGreeting;
        private final IUserC mUser;

        @Inject
        public GreeterC(@Greeting final String greeting, final IUserC user) {
            mGreeting = greeting;
            mUser = user;
            mSaidGreeting = "";
        }

        @Override
        public void greet() {
            mSaidGreeting = mGreeting + " " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleC extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn("World");
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn("Hello");
            whenRequestedInstanceOf(IUserC.class).thenInstantiate(UserC.class);
            whenRequestedInstanceOf(IGreeterC.class).thenInstantiate(GreeterC.class);
        }
    }

    @Test
    public void annotatedInjectionsC() {
        Factory.addModuleClass(ModuleC.class);
        IGreeterC greeter = Factory.getInstance(IGreeterC.class);
        Assert.assertEquals("", ((GreeterC) greeter).mSaidGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterC) greeter).mSaidGreeting);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Named injections in fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserF {

        String getUserName();
    }

    private interface IGreeterF {

        void greet();
    }

    private static class UserF implements IUserF {

        @Inject @UserName private String mUserName;

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterF implements IGreeterF {

        private                   String mSaidGreeting;
        @Inject @Greeting private String mGreeting;
        @Inject private           IUserF mUser;

        public GreeterF() {
            mSaidGreeting = "";
        }

        @Override
        public void greet() {
            mSaidGreeting = mGreeting + " " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleF extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn("World");
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn("Hello");
            whenRequestedInstanceOf(IUserF.class).thenInstantiate(UserF.class);
            whenRequestedInstanceOf(IGreeterF.class).thenInstantiate(GreeterF.class);
        }
    }

    @Test
    public void annotatedInjectionsF() {
        Factory.addModuleClass(ModuleF.class);
        IGreeterF greeter = Factory.getInstance(IGreeterF.class);
        Assert.assertEquals("", ((GreeterF) greeter).mSaidGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterF) greeter).mSaidGreeting);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Named injections in methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IUserM {

        String getUserName();
    }

    private interface IGreeterM {

        void greet();
    }

    private static class UserM implements IUserM {

        private String mUserName;

        @Inject
        public void setUserName(@UserName final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class GreeterM implements IGreeterM {

        private         String mSaidGreeting;
        private         String mGreeting;
        @Inject private IUserM mUser;

        public GreeterM() {
            mSaidGreeting = "";
        }

        @Inject
        public void setGreeting(@Greeting final String greeting) {
            mGreeting = greeting;
        }

        @Override
        public void greet() {
            mSaidGreeting = mGreeting + " " + mUser.getUserName() + "!";
        }
    }

    public static class ModuleM extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn("World");
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn("Hello");
            whenRequestedInstanceOf(IUserM.class).thenInstantiate(UserM.class);
            whenRequestedInstanceOf(IGreeterM.class).thenInstantiate(GreeterM.class);
        }
    }

    @Test
    public void annotatedInjectionsM() {
        Factory.addModuleClass(ModuleM.class);
        IGreeterM greeter = Factory.getInstance(IGreeterM.class);
        Assert.assertEquals("", ((GreeterM) greeter).mSaidGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", ((GreeterM) greeter).mSaidGreeting);
    }
}
