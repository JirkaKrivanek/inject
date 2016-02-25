package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link BinderSingleton} class.
 */
public class BinderSingletonTest {

    @Test
    public void constructor() {
        final Factory factory = Mockito.mock(Factory.class);
        final String value = "Hello World!";
        final BinderSingleton<String> binder = new BinderSingleton(factory, value);
        Assert.assertNotNull(binder);
        Assert.assertSame(factory, binder.mFactory);
    }

    @Test
    public void get() {
        final Factory factory = Mockito.mock(Factory.class);
        final String value = "Hello World!";
        final BinderSingleton<String> binder = new BinderSingleton(factory, value);
        Assert.assertSame(value, binder.get());
    }
}
