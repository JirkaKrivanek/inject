package com.kk.inject.integration.singleton.factory;

import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Module;
import com.kk.inject.Provides;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the provider injections.
 */
public class ProviderInjectionTest {

    private static final String USER_NAME = "R.I.P. Steve!";

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Simple provider
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface SimpleProviderUserI {

        String getUserName();
    }

    private static class SimpleProviderUser implements SimpleProviderUserI {

        @Override
        public String getUserName() {
            return USER_NAME;
        }
    }

    private static class SimpleProviderProvider {

        SimpleProviderUserI getUser() {
            return new SimpleProviderUser();
        }
    }

    public static class SimpleProviderModule extends Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(SimpleProviderUserI.class).thenProvide(new SimpleProviderProvider(), "getUser");
        }
    }

    @Test
    public void simpleProvider() {
        Factory.addModuleClass(SimpleProviderModule.class);
        final SimpleProviderUserI user = Factory.getInstance(SimpleProviderUserI.class);
        Assert.assertEquals(USER_NAME, user.getUserName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Additional provider object
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface AdditionalProviderUserI {

        String getUserName();
    }

    private static class AdditionalProviderUser implements AdditionalProviderUserI {

        @Inject @UserName private String mUserName;

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    private static class AdditionalProviderProvider {

        @Inject private Factory mFactory;

        @Provides
        @UserName
        private String getUserName() {
            return USER_NAME;
        }

        @Provides
        private AdditionalProviderUserI getUser() {
            return mFactory.inject(new AdditionalProviderUser());
        }
    }

    public static class AdditionalProviderModule extends Module {

        @Override
        protected void defineBindings() {
            addProviderObject(new AdditionalProviderProvider());
        }
    }

    @Test
    public void additionalProvider() {
        Factory.addModuleClass(AdditionalProviderModule.class);
        final AdditionalProviderUserI user = Factory.getInstance(AdditionalProviderUserI.class);
        Assert.assertEquals(USER_NAME, user.getUserName());
    }
}
