package com.kk.inject.integration.singleton.factory;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Module;
import com.kk.inject.Singleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the injection of the factory itself.
 */
public class FactoryInjectionTest {

    private static final String USER_NAME = "John";
    private static final String PASS_WORD = "1234";

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test of the factory injection
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface InjectFactoryUserI {

        String getLogin();
    }

    private static class InjectFactoryUser implements InjectFactoryUserI {

        @Inject private           InjectFactoryServiceI mService;
        @Inject @UserName private String                mUserName;

        @Override
        public String getLogin() {
            return mUserName + ":" + mService.getPassword();
        }
    }

    @Singleton
    private interface InjectFactoryServiceI {

        String getPassword();

        InjectFactoryUserI getUser();
    }

    private static class InjectFactoryService implements InjectFactoryServiceI {

        @Inject @Password private String  mPassword;
        @Inject private           Factory mFactory;

        @Override
        public String getPassword() {
            return mPassword;
        }

        @Override
        public InjectFactoryUserI getUser() {
            return mFactory.get(InjectFactoryUserI.class);
        }
    }

    public static class InjectFactoryModule extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn(USER_NAME);
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Password.class).thenReturn(PASS_WORD);
            whenRequestedInstanceOf(InjectFactoryServiceI.class).thenInstantiate(InjectFactoryService.class);
            whenRequestedInstanceOf(InjectFactoryUserI.class).thenInstantiate(InjectFactoryUser.class);
        }
    }

    @Test
    public void injectFactory() {
        Factory.addModuleClass(InjectFactoryModule.class);
        final InjectFactoryServiceI service = Factory.getInstance(InjectFactoryServiceI.class);
        final InjectFactoryUserI user = service.getUser();
        Assert.assertEquals(USER_NAME + ":" + PASS_WORD, user.getLogin());
    }
}
