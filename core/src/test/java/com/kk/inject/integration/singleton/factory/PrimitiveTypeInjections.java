package com.kk.inject.integration.singleton.factory;

import com.kk.inject.AbstractModule;
import com.kk.inject.Factory;
import com.kk.inject.Inject;
import com.kk.inject.Named;
import com.kk.inject.Provides;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the primitive types injections.
 */
public class PrimitiveTypeInjections {

    private static final String NAME_OP_A = "opA";
    private static final String NAME_OP_B = "opB";
    private static final String NAME_OP_C = "opC";

    @Before
    public void resetSingletonFactory() {
        Factory.resetFactoryDefinition();
        Factory.resetSingletonFactory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Byte
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ByteI {

        byte getResult();
    }

    private static class ByteCI implements ByteI {

        @Inject @Named(NAME_OP_A) private byte mOpA;
        private                           byte mOpB;
        private                           byte mOpC;

        @Inject
        public ByteCI(@Named(NAME_OP_B) final byte opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final byte opC) {
            mOpC = opC;
        }

        @Override
        public byte getResult() {
            return (byte) (mOpA + mOpB + mOpC);
        }
    }

    public static class ByteM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private byte getA() {
            return (byte) 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(byte.class).ifNamed(NAME_OP_B).thenReturn((byte) 2);
            whenRequestedInstanceOf(Byte.class).ifNamed(NAME_OP_C).thenReturn((byte) 3);
            whenRequestedInstanceOf(ByteI.class).thenInstantiate(ByteCI.class);
        }
    }

    @Test
    public void bytes() {
        Factory.addModuleClass(ByteM.class);
        final ByteI byteI1 = Factory.getInstance(ByteI.class);
        Assert.assertEquals((byte) 6, byteI1.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Short
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface ShortI {

        short getResult();
    }

    private static class ShortCI implements ShortI {

        @Inject @Named(NAME_OP_A) private short mOpA;
        private                           short mOpB;
        private                           short mOpC;

        @Inject
        public ShortCI(@Named(NAME_OP_B) final short opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final short opC) {
            mOpC = opC;
        }

        @Override
        public short getResult() {
            return (short) (mOpA + mOpB + mOpC);
        }
    }

    public static class ShortM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private short getA() {
            return (short) 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(short.class).ifNamed(NAME_OP_B).thenReturn((short) 2);
            whenRequestedInstanceOf(Short.class).ifNamed(NAME_OP_C).thenReturn((short) 3);
            whenRequestedInstanceOf(ShortI.class).thenInstantiate(ShortCI.class);
        }
    }

    @Test
    public void shorts() {
        Factory.addModuleClass(ShortM.class);
        final ShortI shortI1 = Factory.getInstance(ShortI.class);
        Assert.assertEquals((short) 6, shortI1.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Integer
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface IntegerI {

        int getResult();
    }

    private static class IntegerCI implements IntegerI {

        @Inject @Named(NAME_OP_A) private int mOpA;
        private                           int mOpB;
        private                           int mOpC;

        @Inject
        public IntegerCI(@Named(NAME_OP_B) final int opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final int opC) {
            mOpC = opC;
        }

        @Override
        public int getResult() {
            return mOpA + mOpB + mOpC;
        }
    }

    public static class IntegerM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private int getA() {
            return 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(int.class).ifNamed(NAME_OP_B).thenReturn(2);
            whenRequestedInstanceOf(Integer.class).ifNamed(NAME_OP_C).thenReturn(3);
            whenRequestedInstanceOf(IntegerI.class).thenInstantiate(IntegerCI.class);
        }
    }

    @Test
    public void ints() {
        Factory.addModuleClass(IntegerM.class);
        final IntegerI intI1 = Factory.getInstance(IntegerI.class);
        Assert.assertEquals(6, intI1.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Long
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface LongI {

        long getResult();
    }

    private static class LongCI implements LongI {

        @Inject @Named(NAME_OP_A) private long mOpA;
        private                           long mOpB;
        private                           long mOpC;

        @Inject
        public LongCI(@Named(NAME_OP_B) final long opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final long opC) {
            mOpC = opC;
        }

        @Override
        public long getResult() {
            return mOpA + mOpB + mOpC;
        }
    }

    public static class LongM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private long getA() {
            return (long) 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(long.class).ifNamed(NAME_OP_B).thenReturn((long) 2);
            whenRequestedInstanceOf(Long.class).ifNamed(NAME_OP_C).thenReturn((long) 3);
            whenRequestedInstanceOf(LongI.class).thenInstantiate(LongCI.class);
        }
    }

    @Test
    public void longs() {
        Factory.addModuleClass(LongM.class);
        final LongI longI1 = Factory.getInstance(LongI.class);
        Assert.assertEquals((long) 6, longI1.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Float
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface FloatI {

        float getResult();
    }

    private static class FloatCI implements FloatI {

        @Inject @Named(NAME_OP_A) private float mOpA;
        private                           float mOpB;
        private                           float mOpC;

        @Inject
        public FloatCI(@Named(NAME_OP_B) final float opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final float opC) {
            mOpC = opC;
        }

        @Override
        public float getResult() {
            return mOpA + mOpB + mOpC;
        }
    }

    public static class FloatM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private float getA() {
            return (float) 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(float.class).ifNamed(NAME_OP_B).thenReturn((float) 2);
            whenRequestedInstanceOf(Float.class).ifNamed(NAME_OP_C).thenReturn((float) 3);
            whenRequestedInstanceOf(FloatI.class).thenInstantiate(FloatCI.class);
        }
    }

    @Test
    public void floats() {
        Factory.addModuleClass(FloatM.class);
        final FloatI floatI1 = Factory.getInstance(FloatI.class);
        Assert.assertEquals((float) 6, floatI1.getResult(), 0.0001);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Double
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface DoubleI {

        double getResult();
    }

    private static class DoubleCI implements DoubleI {

        @Inject @Named(NAME_OP_A) private double mOpA;
        private                           double mOpB;
        private                           double mOpC;

        @Inject
        public DoubleCI(@Named(NAME_OP_B) final double opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final double opC) {
            mOpC = opC;
        }

        @Override
        public double getResult() {
            return mOpA + mOpB + mOpC;
        }
    }

    public static class DoubleM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private double getA() {
            return (double) 1;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(double.class).ifNamed(NAME_OP_B).thenReturn((double) 2);
            whenRequestedInstanceOf(Double.class).ifNamed(NAME_OP_C).thenReturn((double) 3);
            whenRequestedInstanceOf(DoubleI.class).thenInstantiate(DoubleCI.class);
        }
    }

    @Test
    public void doubles() {
        Factory.addModuleClass(DoubleM.class);
        final DoubleI doubleI1 = Factory.getInstance(DoubleI.class);
        Assert.assertEquals((double) 6, doubleI1.getResult(), 0.0001);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Boolean
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface BooleanI {

        boolean getResult();
    }

    private static class BooleanCI implements BooleanI {

        @Inject @Named(NAME_OP_A) private boolean mOpA;
        private                           boolean mOpB;
        private                           boolean mOpC;

        @Inject
        public BooleanCI(@Named(NAME_OP_B) final boolean opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final boolean opC) {
            mOpC = opC;
        }

        @Override
        public boolean getResult() {
            return mOpA | mOpB | mOpC;
        }
    }

    public static class BooleanM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private boolean getA() {
            return false;
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(boolean.class).ifNamed(NAME_OP_B).thenReturn(false);
            whenRequestedInstanceOf(Boolean.class).ifNamed(NAME_OP_C).thenReturn(false);
            whenRequestedInstanceOf(BooleanI.class).thenInstantiate(BooleanCI.class);
        }
    }

    @Test
    public void booleans() {
        Factory.addModuleClass(BooleanM.class);
        final BooleanI booleanI1 = Factory.getInstance(BooleanI.class);
        Assert.assertEquals(false, booleanI1.getResult());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Char
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private interface CharI {

        String getResult();
    }

    private static class CharCI implements CharI {

        @Inject @Named(NAME_OP_A) private char mOpA;
        private                           char mOpB;
        private                           char mOpC;

        @Inject
        public CharCI(@Named(NAME_OP_B) final char opB) {
            mOpB = opB;
        }

        @Inject
        public void setOpC(@Named(NAME_OP_C) final char opC) {
            mOpC = opC;
        }

        @Override
        public String getResult() {
            return "" + mOpA + mOpB + mOpC;
        }
    }

    public static class CharM extends AbstractModule {

        @Provides
        @Named(NAME_OP_A)
        private char getA() {
            return 'A';
        }

        @Override
        protected void defineBindings() {
            whenRequestedInstanceOf(char.class).ifNamed(NAME_OP_B).thenReturn('B');
            whenRequestedInstanceOf(Character.class).ifNamed(NAME_OP_C).thenReturn('C');
            whenRequestedInstanceOf(CharI.class).thenInstantiate(CharCI.class);
        }
    }

    @Test
    public void chars() {
        Factory.addModuleClass(CharM.class);
        final CharI charI1 = Factory.getInstance(CharI.class);
        Assert.assertEquals("ABC", charI1.getResult());
    }
}
