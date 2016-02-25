package com.kk.inject;

import java.lang.annotation.Annotation;

/**
 * Container uniquely identifying the binding.
 * <p/>
 * It is used as a key to the map container. The value is one of the {@link Binder} implementations.
 */
final class BindingId {

    @NotNull private final  Class<?>                    mClass;
    @Nullable private final String                      mName;
    @Nullable private final Class<? extends Annotation> mAnnotation;

    /**
     * Constructs the binding ID.
     *
     * @param clazz
     *         The class which this binding realizes. Never {@code null}.
     * @param name
     *         The name which this binding realizes - see the {@link Named} annotation. It further specializes the
     *         binding. Can be {@code null}.
     * @param annotation
     *         The annotation which this binding realizes. It further specializes the binding. Can be {@code null}.
     */
    public BindingId(@NotNull final Class<?> clazz,
                     @Nullable final String name,
                     @Nullable final Class<? extends Annotation> annotation) {
        mClass = clazz;
        mName = name;
        mAnnotation = annotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = -1845873496;
        result += mClass.hashCode();
        if (mName != null) {
            result += mName.hashCode();
        }
        if (mAnnotation != null) {
            result += mAnnotation.hashCode();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        // Null object to compare to
        if (obj == null) {
            return false;
        }
        // Identity
        if (this != obj) {
            // Binding ID class
            if (!(obj instanceof BindingId)) {
                return false;
            }
            final BindingId bi = (BindingId) obj;
            // Class
            if (mClass != bi.mClass) {
                return false;
            }
            // Name
            if (mName == null || bi.mName == null) {
                if (mName != null || bi.mName != null) {
                    return false;
                }
            } else {
                if (!mName.equals(bi.mName)) {
                    return false;
                }
            }
            // Annotation
            if (mAnnotation == null || bi.mAnnotation == null) {
                if (mAnnotation != null || bi.mAnnotation != null) {
                    return false;
                }
            } else {
                if (!mAnnotation.equals(bi.mAnnotation)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mClass.getSimpleName());
        if (mName != null) {
            sb.append(":");
            sb.append(mName);
        }
        if (mAnnotation != null) {
            sb.append(":");
            sb.append(mAnnotation.getSimpleName());
        }
        return sb.toString();
    }
}
