package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;

/**
 * tests the {@link Utils} class.
 */
public class UtilsTest {

    private interface I {}

    private static class IC implements I {}

    private static class ICC extends IC {}

    @Test
    public void checkParameterTypes_nullAndNull() {
        final Class<?>[] requiredTypes = null;
        final Object[] parameters = null;
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_nullAndZero() {
        final Class<?>[] requiredTypes = null;
        final Object[] parameters = new Object[0];
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_zeroAndNull() {
        final Class<?>[] requiredTypes = new Class<?>[0];
        final Object[] parameters = null;
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_nullAndNonzero() {
        final Class<?>[] requiredTypes = null;
        final Object[] parameters = new Object[1];
        Assert.assertFalse(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_nonzeroAndNull() {
        final Class<?>[] requiredTypes = new Class<?>[1];
        final Object[] parameters = null;
        Assert.assertFalse(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_differentLength() {
        final Class<?>[] requiredTypes = new Class<?>[1];
        final Object[] parameters = new Object[2];
        Assert.assertFalse(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_missMatch() {
        final Object[] parameters = new Object[]{"Hello", new StringBuilder()};
        final Class<?>[] requiredTypes = new Class<?>[]{StringBuilder.class, String.class};
        Assert.assertFalse(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_missMatchPrimitiveWithNull() {
        final Object[] parameters = new Object[]{"Hello", null};
        final Class<?>[] requiredTypes = new Class<?>[]{String.class, int.class};
        Assert.assertFalse(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_matchWithNull() {
        final Object[] parameters = new Object[]{"Hello", null};
        final Class<?>[] requiredTypes = new Class<?>[]{String.class, StringBuilder.class};
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_match() {
        final Object[] parameters = new Object[]{"Hello", new StringBuilder()};
        final Class<?>[] requiredTypes = new Class<?>[]{String.class, StringBuilder.class};
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }

    @Test
    public void checkParameterTypes_assignable() {
        final Object[] parameters = new Object[]{new I() {}, new IC(), new ICC(), new ICC()};
        final Class<?>[] requiredTypes = new Class<?>[]{I.class, I.class, IC.class, ICC.class};
        Assert.assertTrue(Utils.checkParameterTypes(requiredTypes, parameters));
    }
}
