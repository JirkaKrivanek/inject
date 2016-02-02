package com.kk.inject.internal;

import com.kk.inject.InjectException;
import com.kk.inject.NotNull;
import com.kk.inject.Nullable;

import java.lang.annotation.Annotation;

/**
 * Binding identifier.
 * <p/>
 * It is used on many places to lookup the bindings via the maps.
 */
public final class BindingId {
    private Class<?> mClazz;
    private String mName;
    private Class<? extends Annotation> mAnnotation;
    private Class<? extends Annotation>[] mAnnotations;
    private boolean mBindingAlreadyUsed;

    /**
     * Constructs the binding identifier.
     *
     * @param clazz
     *         The binding class. Never {@code null}.
     * @param name
     *         The binding name. If {@code null} then only bindings without name match.
     * @param annotation
     *         The annotation. If {@code null} then only bindings without annotation match.
     * @param annotations
     *         The annotations. If {@code null} then only bindings without annotation match.
     */
    public BindingId(@NotNull final Class<?> clazz, @Nullable final String name, @Nullable final Class<? extends Annotation> annotation, @Nullable final Class<? extends Annotation>[] annotations) {
        mClazz = clazz;
        mName = name;
        if (annotation != null && annotations != null) {
            throw new InjectException(Constants.ERROR_BINDING_ID_CANNOT_HAVE_BOTH_ANNOTATIONS);
        }
        mAnnotation = annotation;
        mAnnotations = annotations;
        mBindingAlreadyUsed = false;
    }

    /**
     * Retrieves the class constituting the binding ID.
     *
     * @return The class. Never {@code null}.
     */
    @NotNull
    public Class<?> getClazz() {
        return mClazz;
    }

    /**
     * Changes the class constituting the binding ID.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Be very careful with changing the binding ID data. It can completely break the
     * functionality by non-working map keys.</dd></dl>
     *
     * @param clazz
     *         The class. Never {@code null}.
     */
    public void setClazz(@NotNull final Class<?> clazz) {
        if (mBindingAlreadyUsed) {
            throw new InjectException(Constants.ERROR_BINDING_ALREADY_USED);
        }
        mClazz = clazz;
    }

    /**
     * Retrieves the name constituting the binding ID.
     *
     * @return The name. Can be {@code null}.
     */
    @Nullable
    public String getName() {
        return mName;
    }

    /**
     * Changes the name constituting the binding ID.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Be very careful with changing the binding ID data. It can completely break the
     * functionality by non-working map keys.</dd></dl>
     *
     * @param name
     *         The name. Can be {@code null}.
     */
    public void setName(@Nullable final String name) {
        if (mBindingAlreadyUsed) {
            throw new InjectException(Constants.ERROR_BINDING_ALREADY_USED);
        }
        mName = name;
    }

    /**
     * Retrieves the annotation constituting the binding ID.
     *
     * @return The annotation. Can be {@code null}.
     */
    @Nullable
    public Class<? extends Annotation> getAnnotation() {
        return mAnnotation;
    }

    /**
     * Changes the annotation constituting the binding ID.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Be very careful with changing the binding ID data. It can completely break the
     * functionality by non-working map keys.</dd></dl>
     *
     * @param annotation
     *         The annotation. Can be {@code null}.
     */
    public void setAnnotation(@Nullable final Class<? extends Annotation> annotation) {
        if (mBindingAlreadyUsed) {
            throw new InjectException(Constants.ERROR_BINDING_ALREADY_USED);
        }
        mAnnotation = annotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BindingId)) {
            return false;
        }
        final BindingId other = (BindingId) obj;
        if (!other.mClazz.equals(mClazz)) {
            return false;
        }
        if (other.mName == null && mName != null) {
            return false;
        }
        if (other.mName != null && mName == null) {
            return false;
        }
        if (mName != null && !other.mName.equals(mName)) {
            return false;
        }
        if (mAnnotation != null && other.mAnnotation == null && other.mAnnotations == null) {
            return false;
        }
        if (other.mAnnotation != null && mAnnotation == null && mAnnotations == null) {
            return false;
        }
        if (other.mAnnotation != null && mAnnotation != null && !other.mAnnotation.equals(mAnnotation)) {
            return false;
        }
        if (mAnnotation != null && other.mAnnotation == null && other.mAnnotations != null && !Utils.contains(other.mAnnotations, mAnnotation)) {
            return false;
        }
        if (other.mAnnotation != null && mAnnotation == null && mAnnotations != null && !Utils.contains(mAnnotations, other.mAnnotation)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        mBindingAlreadyUsed = true;
        int result = 856454263;
        result += mClazz.hashCode();
        if (mName != null) {
            result += mName.hashCode();
        }
        return result;
    }

    @Override
    @NotNull
    public String toString() {
        return mClazz.getName() + ": " + mName + ": " + mAnnotation;
    }
}
