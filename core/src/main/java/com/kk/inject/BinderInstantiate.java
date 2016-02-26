package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Binding implementation: Instantiates class.
 */
class BinderInstantiate<T> extends Binder<T> {

    @NotNull private final Class<? extends T> mClassToInstantiate;
    private final          boolean            mIsSingleton;
    @NotNull private       T                  mSingleton;

    /**
     * Constructs the binder.
     *
     * @param factory
     *         The factory which the binder is related to. Never {@code null}.
     * @param classToInstantiate
     *         The class to instantiate. Never {@code null}.
     * @param isSingleton
     *         The singleton flag. If {@code true} then the class is only instantiated once per factory.
     */
    BinderInstantiate(@NotNull final Factory factory,
                      @NotNull final Class<? extends T> classToInstantiate,
                      final boolean isSingleton) {
        super(factory);
        mClassToInstantiate = classToInstantiate;
        mIsSingleton = isSingleton;
        mSingleton = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    T get(@NotNull final Object... parameters) {
        // If singleton and already instantiated then just return it
        if (mIsSingleton && mSingleton != null) {
            return mSingleton;
        }
        // Otherwise instantiate
        final T result = instantiate(parameters);
        // And update singleton is configured so
        if (mIsSingleton) {
            mSingleton = result;
        }
        // Ask factory to (may be later) inject everything to the just instantiated object
        mFactory.injectObject(result);
        // Done
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates the class.
     * <p/>
     * It just decides which method to use to locate the constructor.
     *
     * @param parameters
     *         The parameters to optionally pass to the newly created object. Can be empty.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T instantiate(@NotNull final Object... parameters) {
        @SuppressWarnings("unchecked") final Constructor<T>[] constructors = (Constructor<T>[]) mClassToInstantiate.getDeclaredConstructors();
        if (parameters.length > 0) {
            return instantiateWithParameters(constructors, parameters);
        } else {
            return instantiateWithInjection(constructors);
        }
    }

    /**
     * Instantiates the class using the first constructor annotated with the {@link Inject} or the default (with out
     * parameters) constructor.
     * <p/>
     * The parameters for that constructor are automatically collected using the injection.
     *
     * @param constructors
     *         The constructors list to locate the suitable constructor with in.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T instantiateWithInjection(@NotNull final Constructor<T>[] constructors) {
        // Find suitable constructor - the first annotated one
        Constructor<T> constructor = null;
        Constructor<T> defaultConstructor = null;
        for (final Constructor<T> con : constructors) {
            if (con.isAnnotationPresent(Inject.class)) {
                constructor = con;
                break;
            }
            final Class<?>[] parameterTypes = con.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length <= 0) {
                defaultConstructor = con;
            }
        }
        // If no injected constructor then try to use the default one
        if (constructor == null) {
            constructor = defaultConstructor;
        }
        // Check have constructor
        throwIfNoConstructor(constructor);
        // Prepare the parameters
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        final Object[] parameters = mFactory.collectParametersToInject(parameterTypes, parameterAnnotations);
        // Invoke the constructor to create the instance
        return invokeConstructorWithParameters(constructor, parameters);
    }

    /**
     * Instantiates the class using the constructor matching the types of the supplied parameters list.
     *
     * @param constructors
     *         The constructors list to locate the suitable constructor with in.
     * @param parameters
     *         Te parameters list which the constructor shall be located according to. If {@code null} then the NO
     *         parameters constructor MUST be present.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T instantiateWithParameters(@NotNull Constructor<T>[] constructors, @Nullable final Object... parameters) {
        // Find suitable constructor - by parameter types
        Constructor<T> constructor = null;
        for (final Constructor<T> con : constructors) {
            final Class<?>[] parameterTypes = con.getParameterTypes();
            if (Utils.checkParameterTypes(parameterTypes, parameters)) {
                constructor = con;
            }
        }
        // Check have constructor
        throwIfNoConstructor(constructor);
        // Invoke the constructor to create the instance
        return invokeConstructorWithParameters(constructor, parameters);
    }

    /**
     * If the specified constructor is {@code null} then builds and throws runtime exception (as it clearly is a
     * programming bug).
     *
     * @param constructor
     *         The constructor to check.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    private void throwIfNoConstructor(@Nullable final Constructor<T> constructor) {
        if (constructor == null) {
            throw new InjectException(ErrorStrings.NO_SUITABLE_CONSTRUCTOR, mClassToInstantiate.getName());
        }
    }

    /**
     * Invokes the specified constructor passing the specified parameters list to it.
     *
     * @param constructor
     *         The constructor to invoke. Never {@code null}.
     * @param parameters
     *         The list of parameters to pass to the constructor. If {@code null} then NO parameters constructor is
     *         expected.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T invokeConstructorWithParameters(@NotNull final Constructor<T> constructor,
                                              @Nullable final Object[] parameters) {
        try {
            final boolean setAccessible = !constructor.isAccessible();
            if (setAccessible) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(parameters);
            } finally {
                if (setAccessible) {
                    constructor.setAccessible(false);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InjectException(e, ErrorStrings.FAILED_TO_INSTANTIATE_CLASS, mClassToInstantiate.getName());
        }
    }
}
