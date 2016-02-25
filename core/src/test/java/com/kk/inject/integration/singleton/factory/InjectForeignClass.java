package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the injection into the class which actually is foreign.
 */
public class InjectForeignClass {

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class Greeter {

        private                   String mSaidGreeting;
        @Inject @Greeting private String mGreeting;
        private                   String mSeparator;
        @Inject @UserName private String mUserName;
        private                   String mEnd;

        @Inject
        public void setup(@Named("separator") final String separator, @Named("end") final String end) {
            mSeparator = separator;
            mEnd = end;
        }

        public void greet() {
            mSaidGreeting = mGreeting + mSeparator + mUserName + mEnd;
        }
    }

    public static class Module extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(Greeting.class).thenReturn("Hello");
            whenRequestedInstanceOf(String.class).ifNamed("separator").thenReturn(" ");
            whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn("World");
            whenRequestedInstanceOf(String.class).ifNamed("end").thenReturn("!");
        }
    }

    @Test
    public void foreignInjection() {
        Factory.addModuleClass(Module.class);
        final Greeter greeter = new Greeter();
        Factory.injectInstance(greeter);
        Assert.assertNull(greeter.mSaidGreeting);
        greeter.greet();
        Assert.assertEquals("Hello World!", greeter.mSaidGreeting);
    }
}
