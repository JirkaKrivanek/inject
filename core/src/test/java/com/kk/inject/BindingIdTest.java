package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Tests the {@link BindingId} class.
 */
public final class BindingIdTest {

    private static class C1 {

        @Nullable
        public Object get() {
            return null;
        }

        @NotNull
        public Object ensure() {
            return null;
        }
    }

    private static class C2 {}

    @Test
    public void constructor() {
        final BindingId bindingId = new BindingId(C1.class, null, null);
        Assert.assertNotNull(bindingId);
    }

    @Test
    public void equalsAndHash() {
        // Prepare
        Annotation a1 = null;
        Annotation a2 = null;
        for (Method m : C1.class.getDeclaredMethods()) {
            final Annotation[] annotations = m.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0) {
                Annotation annotation = annotations[0];
                if (a1 == null) {
                    a1 = annotation;
                } else if (a2 == null) {
                    a2 = annotation;
                } else {
                    break;
                }
            }
        }
        // All combinations
        final Class<?> classes[] = new Class<?>[]{C1.class, C2.class};
        final String names[] = new String[]{null, "name 1", "name 2", "name 3"};
        final Annotation annotations[] = new Annotation[]{null, a1, a2};
        final String s = "Hello World";
        for (final Class<?> c1 : classes) {
            for (final Class<?> c2 : classes) {
                for (final String n1 : names) {
                    for (final String n2 : names) {
                        for (final Annotation an1 : annotations) {
                            for (final Annotation an2 : annotations) {
                                // Prepare binding IDs
                                final BindingId b1 = new BindingId(c1, n1, an1);
                                final BindingId b2 = new BindingId(c2, n2, an2);
                                // Compare both to else what
                                Assert.assertFalse(b1.equals(null));
                                Assert.assertFalse(b1.equals(s));
                                Assert.assertFalse(s.equals(b1));
                                Assert.assertFalse(b2.equals(null));
                                Assert.assertFalse(b2.equals(s));
                                Assert.assertFalse(s.equals(b2));
                                // Check
                                final boolean classEquals = c1 == c2;
                                final boolean nameEquals = n1 == n2 || (n1 != null && n2 != null && n1.equals(n2));
                                final boolean anEquals = an1 == an2 || (an1 != null && an2 != null && an1.equals(an2));
                                if (classEquals && nameEquals && anEquals) {
                                    Assert.assertTrue(b1.equals(b2));
                                    Assert.assertTrue(b2.equals(b1));
                                    Assert.assertEquals(b1.hashCode(), b2.hashCode());
                                } else {
                                    Assert.assertFalse(b1.equals(b2));
                                    Assert.assertFalse(b2.equals(b1));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
