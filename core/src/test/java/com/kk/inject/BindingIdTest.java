package com.kk.inject;

import com.kk.inject.integration.singleton.factory.Greeting;
import com.kk.inject.integration.singleton.factory.UserName;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.annotation.Annotation;

/**
 * Tests the {@link BindingId} class.
 */
public final class BindingIdTest {

    private static class C1 {}

    private static class C2 {}

    @Test
    public void constructor() {
        final String name = "Hello World!";
        final Class<? extends Annotation> annotation = UserName.class;
        final BindingId bindingId = new BindingId(C1.class, name, annotation);
        Assert.assertNotNull(bindingId);
        Assert.assertSame(name, Whitebox.getInternalState(bindingId, "mName"));
        Assert.assertSame(annotation, Whitebox.getInternalState(bindingId, "mAnnotation"));
    }

    @Test
    public void toStringTest() {
        final String name = "Hello World!";
        final Class<? extends Annotation> annotation = UserName.class;
        final BindingId bindingId = new BindingId(C1.class, name, annotation);
        Assert.assertEquals("C1:Hello World!:UserName", bindingId.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void equalsAndHash() {
        // All combinations
        final Class<?> classes[] = new Class<?>[]{C1.class, C2.class};
        final String names[] = new String[]{null, "name 1", "name 2", "name 3"};
        final Class<? extends Annotation> annotations[] = (Class<? extends Annotation>[]) new Class<?>[3];
        annotations[0] = null;
        annotations[1] = Greeting.class;
        annotations[2] = UserName.class;
        final String s = "Hello World";
        for (final Class<?> c1 : classes) {
            for (final Class<?> c2 : classes) {
                for (final String n1 : names) {
                    for (final String n2 : names) {
                        for (final Class<? extends Annotation> an1 : annotations) {
                            for (final Class<? extends Annotation> an2 : annotations) {
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
