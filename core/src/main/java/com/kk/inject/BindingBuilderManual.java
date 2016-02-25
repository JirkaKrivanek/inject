package com.kk.inject;

import java.lang.annotation.Annotation;

/**
 * Builds the binding for the module.
 */
public final class BindingBuilderManual<T> {

    @NotNull private final Factory  mFactory;
    @NotNull private final Class<?> mForClass;

    @Nullable private String     mName;
    @Nullable private Annotation mAnnotation;
    private           boolean    mForceSingleton;

    /**
     * Builds the binding for the specified class.
     *
     * @param factory
     *         The factory which the bindings will belong to. Never {@code null}.
     * @param forClass
     *         The class acting as an interface for which the binding shall be built. Never {@code null}.
     */
    BindingBuilderManual(@NotNull final Factory factory, @NotNull final Class<T> forClass) {
        mFactory = factory;
        mForClass = forClass;

        mName = null;
        mAnnotation = null;
        mForceSingleton = false;
    }

    /**
     * Marks the binding being created as singleton.
     * <p/>
     * The same can be achieved by adding the {@link Singleton} annotation to the interface or implementation class.
     *
     * @return The builder for chaining calls. Never {@code null}.
     */
    @NotNull
    public BindingBuilderManual<T> singleton() {
        mForceSingleton = true;
        return this;
    }

    /**
     * Adds the name condition to the binding being built.
     * <p/>
     * See the {@link Named} annotation.
     *
     * @param name
     *         The name for the binding. Never {@code null}.
     * @return The builder for chaining calls. Never {@code null}.
     */
    @NotNull
    public BindingBuilderManual<T> ifNamed(@NotNull final String name) {
        mName = name;
        return this;
    }

    /**
     * Adds the annotated with condition to the binding being built.
     *
     * @param annotation
     *         The annotation for the binding. Never {@code null}.
     * @return The builder for chaining calls. Never {@code null}.
     */
    @NotNull
    public BindingBuilderManual<T> ifAnnotatedWith(@NotNull final Annotation annotation) {
        mAnnotation = annotation;
        return this;
    }

    /**
     * Creates the binder which instantiates the specified class when binding requested.
     *
     * @param classToInstantiate
     *         The class to instantiate. Never {@code null}.
     */
    public void thenInstantiate(@NotNull final Class<? extends T> classToInstantiate) {
        final BindingId bindingId = new BindingId(mForClass, mName, mAnnotation);
        final boolean singleton = isSingleton(classToInstantiate);
        final Binder binding = new BinderInstantiate<>(mFactory, classToInstantiate, singleton);
        mFactory.addBinding(bindingId, binding);
    }

    /**
     * Creates the binder which just returns the specified class when binding requested.
     *
     * @param objectToReturn
     *         The object to return. Never {@code null}.
     */
    public void thenReturn(final T objectToReturn) {
        final BindingId bindingId = new BindingId(mForClass, mName, mAnnotation);
        final Binder<T> binding = new BinderSingleton<T>(mFactory, objectToReturn);
        mFactory.addBinding(bindingId, binding);
    }

    /**
     * Checks whether the binder shall be created as singleton or not.
     *
     * @param classToInstantiate
     *         The class to checks for the singleton annotation. Never {@code null}.
     * @return If singleton then {@code true} else {@code false}.
     */
    private boolean isSingleton(final Class<? extends T> classToInstantiate) {
        return mForceSingleton || mForClass.isAnnotationPresent(Singleton.class) ||
                classToInstantiate.isAnnotationPresent(Singleton.class);
    }
}
