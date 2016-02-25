package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link BinderAbstract} class.
 */
public class BinderAbstractTest {

    private static class BinderAbstractTested extends BinderAbstract {

        public BinderAbstractTested(@NotNull final Factory factory) {
            super(factory);
        }

        @Override
        public Object get(@NotNull final Object... parameters) {
            return null;
        }
    }

    @Test
    public void constructor() {
        final Factory factory = Mockito.mock(Factory.class);
        final BinderAbstract binder = new BinderAbstractTested(factory);
        Assert.assertNotNull(binder);
        Assert.assertSame(factory, binder.mFactory);
    }
}
