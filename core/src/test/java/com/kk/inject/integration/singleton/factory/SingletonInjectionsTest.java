package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Singleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the simple injection.
 */
public class SingletonInjectionsTest {

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Annotated on interface
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Singleton
    private interface Singleton1I {

        void work();
    }

    private static class Singleton1IC implements Singleton1I {

        int mCounter;

        @Inject
        public Singleton1IC() {
            mCounter = 0;
        }

        @Override
        public void work() {
            mCounter++;
        }
    }

    public static class Singleton1Module extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(Singleton1I.class).thenInstantiate(Singleton1IC.class);
        }
    }

    @Test
    public void singleton1() {
        Factory.registerModule(new Singleton1Module());
        final Singleton1I singleton1I1 = Factory.getInstance(Singleton1I.class);
        Assert.assertEquals(0, ((Singleton1IC) singleton1I1).mCounter);
        singleton1I1.work();
        Assert.assertEquals(1, ((Singleton1IC) singleton1I1).mCounter);
        final Singleton1I singleton1I2 = Factory.getInstance(Singleton1I.class);
        Assert.assertEquals(1, ((Singleton1IC) singleton1I2).mCounter);
        singleton1I2.work();
        Assert.assertEquals(2, ((Singleton1IC) singleton1I1).mCounter);
        Assert.assertEquals(2, ((Singleton1IC) singleton1I2).mCounter);
        final Singleton1I singleton1I3 = Factory.getInstance(Singleton1I.class);
        Assert.assertEquals(2, ((Singleton1IC) singleton1I3).mCounter);
        singleton1I3.work();
        Assert.assertEquals(3, ((Singleton1IC) singleton1I1).mCounter);
        Assert.assertEquals(3, ((Singleton1IC) singleton1I2).mCounter);
        Assert.assertEquals(3, ((Singleton1IC) singleton1I3).mCounter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Annotated on implementation
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface Singleton2I {

        void work();
    }

    @Singleton
    private static class Singleton2IC implements Singleton2I {

        int mCounter;

        @Inject
        public Singleton2IC() {
            mCounter = 0;
        }

        @Override
        public void work() {
            mCounter++;
        }
    }

    public static class Singleton2Module extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(Singleton2I.class).thenInstantiate(Singleton2IC.class);
        }
    }

    @Test
    public void singleton2() {
        Factory.registerModule(new Singleton2Module());
        final Singleton2I singleton2I1 = Factory.getInstance(Singleton2I.class);
        Assert.assertEquals(0, ((Singleton2IC) singleton2I1).mCounter);
        singleton2I1.work();
        Assert.assertEquals(1, ((Singleton2IC) singleton2I1).mCounter);
        final Singleton2I singleton2I2 = Factory.getInstance(Singleton2I.class);
        Assert.assertEquals(1, ((Singleton2IC) singleton2I2).mCounter);
        singleton2I2.work();
        Assert.assertEquals(2, ((Singleton2IC) singleton2I1).mCounter);
        Assert.assertEquals(2, ((Singleton2IC) singleton2I2).mCounter);
        final Singleton2I singleton2I3 = Factory.getInstance(Singleton2I.class);
        Assert.assertEquals(2, ((Singleton2IC) singleton2I3).mCounter);
        singleton2I3.work();
        Assert.assertEquals(3, ((Singleton2IC) singleton2I1).mCounter);
        Assert.assertEquals(3, ((Singleton2IC) singleton2I2).mCounter);
        Assert.assertEquals(3, ((Singleton2IC) singleton2I3).mCounter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Forced on binding definition
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface Singleton3I {

        void work();
    }

    private static class Singleton3IC implements Singleton3I {

        int mCounter;

        @Inject
        public Singleton3IC() {
            mCounter = 0;
        }

        @Override
        public void work() {
            mCounter++;
        }
    }

    public static class Singleton3Module extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(Singleton3I.class).singleton().thenInstantiate(Singleton3IC.class);
        }
    }

    @Test
    public void singleton3() {
        Factory.registerModule(new Singleton3Module());
        final Singleton3I singleton3I1 = Factory.getInstance(Singleton3I.class);
        Assert.assertEquals(0, ((Singleton3IC) singleton3I1).mCounter);
        singleton3I1.work();
        Assert.assertEquals(1, ((Singleton3IC) singleton3I1).mCounter);
        final Singleton3I singleton3I2 = Factory.getInstance(Singleton3I.class);
        Assert.assertEquals(1, ((Singleton3IC) singleton3I2).mCounter);
        singleton3I2.work();
        Assert.assertEquals(2, ((Singleton3IC) singleton3I1).mCounter);
        Assert.assertEquals(2, ((Singleton3IC) singleton3I2).mCounter);
        final Singleton3I singleton3I3 = Factory.getInstance(Singleton3I.class);
        Assert.assertEquals(2, ((Singleton3IC) singleton3I3).mCounter);
        singleton3I3.work();
        Assert.assertEquals(3, ((Singleton3IC) singleton3I1).mCounter);
        Assert.assertEquals(3, ((Singleton3IC) singleton3I2).mCounter);
        Assert.assertEquals(3, ((Singleton3IC) singleton3I3).mCounter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Injected to binding definition
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface Singleton4I {

        void work();
    }

    private static class Singleton4IC implements Singleton4I {

        int mCounter;

        @Inject
        public Singleton4IC() {
            mCounter = 0;
        }

        @Override
        public void work() {
            mCounter++;
        }
    }

    public static class Singleton4Module extends AbstractModule {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(Singleton4I.class).thenReturn(new Singleton4IC());
        }
    }

    @Test
    public void singleton4() {
        Factory.registerModule(new Singleton4Module());
        final Singleton4I singleton4I1 = Factory.getInstance(Singleton4I.class);
        Assert.assertEquals(0, ((Singleton4IC) singleton4I1).mCounter);
        singleton4I1.work();
        Assert.assertEquals(1, ((Singleton4IC) singleton4I1).mCounter);
        final Singleton4I singleton4I2 = Factory.getInstance(Singleton4I.class);
        Assert.assertEquals(1, ((Singleton4IC) singleton4I2).mCounter);
        singleton4I2.work();
        Assert.assertEquals(2, ((Singleton4IC) singleton4I1).mCounter);
        Assert.assertEquals(2, ((Singleton4IC) singleton4I2).mCounter);
        final Singleton4I singleton4I3 = Factory.getInstance(Singleton4I.class);
        Assert.assertEquals(2, ((Singleton4IC) singleton4I3).mCounter);
        singleton4I3.work();
        Assert.assertEquals(3, ((Singleton4IC) singleton4I1).mCounter);
        Assert.assertEquals(3, ((Singleton4IC) singleton4I2).mCounter);
        Assert.assertEquals(3, ((Singleton4IC) singleton4I3).mCounter);
    }
}
