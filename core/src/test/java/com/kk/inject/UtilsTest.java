package com.kk.inject;

import com.kk.inject.integration.singleton.factory.Greeting;
import com.kk.inject.integration.singleton.factory.UserName;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * tests the {@link Utils} class.
 */
public class UtilsTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Check parameter types
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Extract name from annotations
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class ExtractNameFromAnnotations {

        @Named("Hello World!")
        public void named() {
        }

        public void notNamed() {
        }
    }

    @Test
    public void extractNameFromAnnotations() throws NoSuchMethodException {
        final Method namedMethod = ExtractNameFromAnnotations.class.getMethod("named");
        final Method notNamedMethod = ExtractNameFromAnnotations.class.getMethod("notNamed");
        final Annotation[] namedMethodAnnotations = namedMethod.getAnnotations();
        final Annotation[] notNamedMethodAnnotations = notNamedMethod.getAnnotations();
        Assert.assertEquals("Hello World!", Utils.extractNameFromAnnotations(namedMethodAnnotations));
        Assert.assertNull(Utils.extractNameFromAnnotations(notNamedMethodAnnotations));
        Assert.assertNull(Utils.extractNameFromAnnotations(null));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Use annotation for binding
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class UseAnnotationForBinding {

        @Named("Hello World!")
        public void notUsedForBinding1() {
        }

        @Inject
        public void notUsedForBinding2() {
        }

        @Singleton
        public void notUsedForBinding3() {
        }

        @NotNull
        public void notUsedForBinding4() {
        }

        @Nullable
        public void notUsedForBinding5() {
        }

        @UserName
        public void usedForBinding1() {
        }

        @Greeting
        public void usedForBinding2() {
        }
    }

    @Test
    public void useAnnotationForBinding() throws NoSuchMethodException {
        final Method notUsedForBinding1M = UseAnnotationForBinding.class.getMethod("notUsedForBinding1");
        final Method notUsedForBinding2M = UseAnnotationForBinding.class.getMethod("notUsedForBinding2");
        final Method notUsedForBinding3M = UseAnnotationForBinding.class.getMethod("notUsedForBinding3");
        final Method notUsedForBinding4M = UseAnnotationForBinding.class.getMethod("notUsedForBinding4");
        final Method notUsedForBinding5M = UseAnnotationForBinding.class.getMethod("notUsedForBinding5");
        final Method usedForBinding1M = UseAnnotationForBinding.class.getMethod("usedForBinding1");
        final Method usedForBinding2M = UseAnnotationForBinding.class.getMethod("usedForBinding2");
        final Annotation notUsedForBinding1A = notUsedForBinding1M.getAnnotations()[0];
        final Annotation notUsedForBinding2A = notUsedForBinding2M.getAnnotations()[0];
        final Annotation notUsedForBinding3A = notUsedForBinding3M.getAnnotations()[0];
        final Annotation notUsedForBinding4A = notUsedForBinding4M.getAnnotations()[0];
        final Annotation notUsedForBinding5A = notUsedForBinding5M.getAnnotations()[0];
        final Annotation usedForBinding1A = usedForBinding1M.getAnnotations()[0];
        final Annotation usedForBinding2A = usedForBinding2M.getAnnotations()[0];
        Assert.assertFalse(Utils.useAnnotationForBinding(notUsedForBinding1A));
        Assert.assertFalse(Utils.useAnnotationForBinding(notUsedForBinding2A));
        Assert.assertFalse(Utils.useAnnotationForBinding(notUsedForBinding3A));
        Assert.assertFalse(Utils.useAnnotationForBinding(notUsedForBinding4A));
        Assert.assertFalse(Utils.useAnnotationForBinding(notUsedForBinding5A));
        Assert.assertTrue(Utils.useAnnotationForBinding(usedForBinding1A));
        Assert.assertTrue(Utils.useAnnotationForBinding(usedForBinding2A));
    }
}
