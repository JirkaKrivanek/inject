package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Named;
import com.kk.inject.Provides;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the module provider injections.
 */
public class ModuleProviderInjectionsTest {

    private static final String NAME_USER_NAME = "userName";
    private static final String USER_NAME      = "John Doe";

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the simple provider without any parameters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface SimpleNoParameterProviderUserI {

        String getUserName();
    }

    public static class SimpleNoParameterProviderM extends AbstractModule {

        @Provides
        private SimpleNoParameterProviderUserI getUser() {
            return new SimpleNoParameterProviderUserI() {
                @Override
                public String getUserName() {
                    return USER_NAME;
                }
            };
        }

        @Override
        protected void defineBindings() {
        }
    }

    @Test
    public void simpleNoParameterProvider() {
        Factory.addModuleClass(SimpleNoParameterProviderM.class);
        final SimpleNoParameterProviderUserI providerI = Factory.getInstance(SimpleNoParameterProviderUserI.class);
        Assert.assertEquals(USER_NAME, providerI.getUserName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the multiple providers without any parameters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface MultipleNoParameterProvidersUserI {

        String getUserName();
    }

    private static class MultipleNoParameterProvidersUserC implements MultipleNoParameterProvidersUserI {

        @Inject private String mUserName;

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    public static class MultipleNoParameterProvidersM extends AbstractModule {

        @Provides
        private String getUserName() {
            return USER_NAME;
        }

        @Provides
        private MultipleNoParameterProvidersUserI getUser() {
            return getFactory().get(MultipleNoParameterProvidersUserC.class);
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(MultipleNoParameterProvidersUserC.class).thenInstantiate(
                    MultipleNoParameterProvidersUserC.class);
        }
    }

    @Test
    public void multipleNoParameterProviders() {
        Factory.addModuleClass(MultipleNoParameterProvidersM.class);
        final MultipleNoParameterProvidersUserI providerI = Factory.getInstance(MultipleNoParameterProvidersUserI.class);
        Assert.assertEquals(USER_NAME, providerI.getUserName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the simple provider with the specified parameters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface SimpleProviderWithSpecifiedParametersI {

        int getResult();
    }

    public static class SimpleProviderWithSpecifiedParametersM extends AbstractModule {

        @Provides
        private SimpleProviderWithSpecifiedParametersI getMultiplication(final int a, final int b) {
            return new SimpleProviderWithSpecifiedParametersI() {
                @Override
                public int getResult() {
                    return a * b;
                }
            };
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(MultipleNoParameterProvidersUserC.class).thenInstantiate(
                    MultipleNoParameterProvidersUserC.class);
        }
    }

    @Test
    public void simpleProviderWithSpecifiedParameters() {
        Factory.addModuleClass(SimpleProviderWithSpecifiedParametersM.class);
        final SimpleProviderWithSpecifiedParametersI result2x3 = Factory.getInstance(
                SimpleProviderWithSpecifiedParametersI.class,
                2,
                3);
        Assert.assertEquals(2 * 3, result2x3.getResult());
        final SimpleProviderWithSpecifiedParametersI result17x222 = Factory.getInstance(
                SimpleProviderWithSpecifiedParametersI.class,
                17,
                222);
        Assert.assertEquals(17 * 222, result17x222.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the simple providers with injected parameters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface SimpleProviderWithInjectedParametersUserI {

        String getUserName();
    }

    private static class SimpleProviderWithInjectedParametersUserC
            implements SimpleProviderWithInjectedParametersUserI {

        private final String mUserName;

        public SimpleProviderWithInjectedParametersUserC(final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    public static class SimpleProviderWithInjectedParametersM extends AbstractModule {

        @Provides
        private SimpleProviderWithInjectedParametersUserI getUser(final String userName) {
            return new SimpleProviderWithInjectedParametersUserC(userName);
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).thenReturn(USER_NAME);
        }
    }

    @Test
    public void simpleProviderWithInjectedParameters() {
        Factory.addModuleClass(SimpleProviderWithInjectedParametersM.class);
        final SimpleProviderWithInjectedParametersUserI user = Factory.getInstance(
                SimpleProviderWithInjectedParametersUserI.class);
        Assert.assertEquals(USER_NAME, user.getUserName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the named provider
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface NamedProviderUserI {

        String getUserName();
    }

    private static class NamedProviderUserC implements NamedProviderUserI {

        private final String mUserName;

        public NamedProviderUserC(final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    public static class NamedProviderM extends AbstractModule {

        @Provides
        @Named(NAME_USER_NAME)
        private String getUserName() {
            return USER_NAME;
        }

        @Provides
        private NamedProviderUserI getUser(@Named(NAME_USER_NAME) final String userName) {
            return new NamedProviderUserC(userName);
        }

        @Override
        protected void defineBindings() {
        }
    }

    @Test
    public void namedProvider() {
        Factory.addModuleClass(NamedProviderM.class);
        final NamedProviderUserI user = Factory.getInstance(NamedProviderUserI.class);
        Assert.assertEquals(USER_NAME, user.getUserName());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Tests the annotated provider
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface AnnotatedProviderUserI {

        String getUserName();
    }

    private static class AnnotatedProviderUserC implements AnnotatedProviderUserI {

        private final String mUserName;

        public AnnotatedProviderUserC(final String userName) {
            mUserName = userName;
        }

        @Override
        public String getUserName() {
            return mUserName;
        }
    }

    public static class AnnotatedProviderM extends AbstractModule {

        @Provides
        @UserName
        private String getUserName() {
            return USER_NAME;
        }

        @Provides
        private AnnotatedProviderUserI getUser(@UserName final String userName) {
            return new AnnotatedProviderUserC(userName);
        }

        @Override
        protected void defineBindings() {
        }
    }

    @Test
    public void annotatedProvider() {
        Factory.addModuleClass(AnnotatedProviderM.class);
        final AnnotatedProviderUserI user = Factory.getInstance(AnnotatedProviderUserI.class);
        Assert.assertEquals(USER_NAME, user.getUserName());
    }
}
