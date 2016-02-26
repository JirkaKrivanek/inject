package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the simple injection.
 */
public class ConstructorInjectionsTest {

    private int mWorkedCounter;

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default constructor used to instantiate
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface DefaultConstructorI {

        void work();
    }

    private static class DefaultConstructorIC implements DefaultConstructorI {

        boolean mWorked;

        public DefaultConstructorIC() {
            mWorked = false;
        }

        @Override
        public void work() {
            mWorked = true;
        }
    }

    public static class DefaultConstructorModule extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(DefaultConstructorI.class).thenInstantiate(DefaultConstructorIC.class);
        }
    }

    @Test
    public void defaultConstructor() {
        Factory.registerModule(new DefaultConstructorModule());
        final DefaultConstructorI defaultConstructorI = Factory.getInstance(DefaultConstructorI.class);
        defaultConstructorI.work();
        Assert.assertTrue(((DefaultConstructorIC) defaultConstructorI).mWorked);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Parametrized constructor used to instantiate
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ParametrizedConstructorI {

        void work();
    }

    private static class ParametrizedConstructorIC implements ParametrizedConstructorI {

        private final ConstructorInjectionsTest mTest;
        private final int                       mCounter;

        public ParametrizedConstructorIC(final ConstructorInjectionsTest test, final Integer counter) {
            mTest = test;
            mCounter = counter;
        }

        @Override
        public void work() {
            mTest.mWorkedCounter = mCounter;
        }
    }

    public static class ParametrizedConstructorModule extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(ParametrizedConstructorI.class).thenInstantiate(ParametrizedConstructorIC.class);
        }
    }

    @Test
    public void parametrizedConstructor() {
        Factory.registerModule(new ParametrizedConstructorModule());
        ParametrizedConstructorI parametrizedConstructorI;
        parametrizedConstructorI = Factory.getInstance(ParametrizedConstructorI.class, this, 17);
        mWorkedCounter = 0;
        parametrizedConstructorI.work();
        Assert.assertEquals(17, mWorkedCounter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Annotated constructor used to instantiate
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface AnnotatedConstructorI {

        void work();
    }

    private static class AnnotatedConstructorIC implements AnnotatedConstructorI {

        boolean mWorked;

        @Inject
        public AnnotatedConstructorIC() {
            mWorked = false;
        }

        @Override
        public void work() {
            mWorked = true;
        }
    }

    public static class AnnotatedConstructorModule extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(AnnotatedConstructorI.class).thenInstantiate(AnnotatedConstructorIC.class);
        }
    }

    @Test
    public void annotatedConstructor() {
        Factory.registerModule(new AnnotatedConstructorModule());
        final AnnotatedConstructorI annotatedConstructorI = Factory.getInstance(AnnotatedConstructorI.class);
        annotatedConstructorI.work();
        Assert.assertTrue(((AnnotatedConstructorIC) annotatedConstructorI).mWorked);
    }
}
