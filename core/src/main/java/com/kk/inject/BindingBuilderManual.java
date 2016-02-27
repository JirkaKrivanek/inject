package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds the binding for the module.
 */
public final class BindingBuilderManual<T> {

    @NotNull private final Factory            mFactory;
    @NotNull private final Class<? extends T> mForClass;

    @NotNull private  List<Class<? extends T>>    mForClasses;
    @Nullable private String                      mName;
    @Nullable private Class<? extends Annotation> mAnnotation;
    private           boolean                     mForceSingleton;

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
     * Adds more classes for which the resulting binding will be used.
     *
     * @param forClass
     *         The second class. Never {@code null}.
     * @return The builder for chaining calls. Never {@code null}.
     */
    @NotNull
    public BindingBuilderManual<T> addForClass(@NotNull final Class<? extends T> forClass) {
        if (mForClasses == null) {
            mForClasses = new ArrayList<>();
        }
        mForClasses.add(forClass);
        return this;
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
    public BindingBuilderManual<T> ifAnnotatedWith(@NotNull final Class<? extends Annotation> annotation) {
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
        final boolean singleton = isSingleton(classToInstantiate);
        final Binder binder = new BinderInstantiate<>(mFactory, classToInstantiate, singleton);
        final BindingId bindingId1 = new BindingId(mForClass, mName, mAnnotation);
        mFactory.addBinding(bindingId1, binder);
        if (mForClasses != null) {
            for (final Class<? extends T> forClass : mForClasses) {
                final BindingId bindingId2 = new BindingId(forClass, mName, mAnnotation);
                mFactory.addBinding(bindingId2, binder);
            }
        }
    }

    /**
     * Creates the binder which just returns the specified class when binding requested.
     *
     * @param objectToReturn
     *         The object to return. Never {@code null}.
     */
    public void thenReturn(@NotNull final T objectToReturn) {
        final Binder<T> binder = new BinderSingleton<>(mFactory, objectToReturn);
        final BindingId bindingId1 = new BindingId(mForClass, mName, mAnnotation);
        mFactory.addBinding(bindingId1, binder);
        if (mForClasses != null) {
            for (final Class<? extends T> forClass : mForClasses) {
                final BindingId bindingId2 = new BindingId(forClass, mName, mAnnotation);
                mFactory.addBinding(bindingId2, binder);
            }
        }
    }

    /**
     * Creates the binder which uses the specified provider when binding requested.
     *
     * @param provider
     *         The provider to be used to retrieve the instance.
     * @param methodName
     *         The name of the method which performs the providing. Never {@code null}.
     */
    public void thenProvide(@NotNull final Object provider, @NotNull String methodName) {
        final Method method;
        try {
            method = provider.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new InjectException(ErrorStrings.PROVIDER_MUST_HAVE_METHOD,
                                      methodName,
                                      provider.getClass().getName());
        }
        final Binder<T> binder = new BinderProvider<>(mFactory, provider, method);
        final BindingId bindingId1 = new BindingId(mForClass, mName, mAnnotation);
        mFactory.addBinding(bindingId1, binder);
        if (mForClasses != null) {
            for (final Class<? extends T> forClass : mForClasses) {
                final BindingId bindingId2 = new BindingId(forClass, mName, mAnnotation);
                mFactory.addBinding(bindingId2, binder);
            }
        }
    }

    /**
     * Creates the binder which uses the specified provider when binding requested.
     *
     * @param provider
     *         The provider to be used to retrieve the instance. The provider MUST implement the method named "get".
     *         Never {@code null}.
     */
    public void thenProvide(@NotNull final Module provider) {
        thenProvide(provider, "get");
    }

    /**
     * Checks whether the binder shall be created as singleton or not.
     *
     * @param classToInstantiate
     *         The class to checks for the singleton annotation. Never {@code null}.
     * @return If singleton then {@code true} else {@code false}.
     */
    private boolean isSingleton(@NotNull final Class<? extends T> classToInstantiate) {
        if (mForceSingleton || mForClass.isAnnotationPresent(Singleton.class) ||
                classToInstantiate.isAnnotationPresent(Singleton.class)) {
            return true;
        }
        if (mForClasses != null) {
            for (final Class<? extends T> forClass : mForClasses) {
                if (forClass.isAnnotationPresent(Singleton.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}
