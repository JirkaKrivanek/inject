package com.kk.inject.integration.multiple.factories;

import com.kk.inject.Factory;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that singletons are actually only single within the same factory.
 */
public class IndependentSingletons {

    private static class C {}

    public static class Module extends com.kk.inject.Module {

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(C.class).singleton().thenInstantiate(C.class);
        }
    }

    @Test
    public void multipleFactories() {
        Factory.addModuleClass(Module.class);

        final Factory f1 = Factory.createFactory();
        final Factory f2 = Factory.createFactory();

        final C c11 = f1.get(C.class);
        final C c12 = f1.get(C.class);

        final C c21 = f2.get(C.class);
        final C c22 = f2.get(C.class);

        Assert.assertSame(c11, c12);
        Assert.assertSame(c21, c22);

        Assert.assertNotSame(c11, c21);
        Assert.assertNotSame(c11, c22);
    }
}
