package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link Binder} class.
 */
public class BinderTest {

    private static class BinderTested extends Binder {

        BinderTested(@NotNull final Factory factory) {
            super(factory);
        }

        @Override
        Module get(@NotNull final Object... parameters) {
            return null;
        }
    }

    @Test
    public void constructor() {
        final Factory factory = Mockito.mock(Factory.class);
        final Binder binder = new BinderTested(factory);
        Assert.assertNotNull(binder);
        Assert.assertSame(factory, binder.mFactory);
    }
}
