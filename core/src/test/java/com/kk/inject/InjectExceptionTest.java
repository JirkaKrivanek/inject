package com.kk.inject;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kk on 25/02/16.
 */
public class InjectExceptionTest {

    @Test
    public void constructorWithOutCause() {
        final InjectException exception = new InjectException("A:%s", "B");
        Assert.assertNotNull(exception);
        Assert.assertEquals("A:B", exception.getMessage());
        Assert.assertNull(exception.getCause());
    }

    @Test
    public void constructorWithCause() {
        final Exception cause = new Exception("CAUSE");
        final InjectException exception = new InjectException(cause, "A:%s", "B");
        Assert.assertNotNull(exception);
        Assert.assertEquals("A:B", exception.getMessage());
        Assert.assertSame(cause, exception.getCause());
    }
}
