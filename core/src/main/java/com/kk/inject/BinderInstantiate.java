package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Binding implementation: Instantiates class.
 */
class BinderInstantiate<T> extends BinderAbstract<T> {

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
    public BinderInstantiate(@NotNull final Factory factory,
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
    public T get(@NotNull final Object... parameters) {
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

    /**
     * Instantiates the class.
     *
     * @param parameters
     *         The parameters to optionally pass to the newly created object. Can be empty.
     * @return The ensured instance. Never {@code null}.
     */
    private T instantiate(@NotNull final Object... parameters) {
        // Find suitable constructor either the first annotated one (higher priority) as injected or by parameter types
        @SuppressWarnings("unchecked") final Constructor<T>[] constructors = (Constructor<T>[]) mClassToInstantiate.getDeclaredConstructors();
        Constructor<T> injectedConstructor = null;
        Constructor<T> parametersConstructor = null;
        for (final Constructor<T> constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                injectedConstructor = constructor;
                break;
            }
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (Utils.checkParameterTypes(parameterTypes, parameters)) {
                parametersConstructor = constructor;
            }
        }
        // Prepare the parameters and constructor
        Constructor<T> constructor = injectedConstructor;
        Object[] params;
        if (constructor == null) {
            // If NO injected constructor then try to use the one by parameters
            constructor = parametersConstructor;
            params = parameters;
        } else {
            // If have injected constructor then collect parameters for it by injection
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            params = mFactory.collectParametersToInject(parameterTypes, parameterAnnotations);
        }
        // Check have constructor
        if (constructor == null) {
            throw new InjectException(ErrorStrings.NO_SUITABLE_CONSTRUCTOR, mClassToInstantiate.getName());
        }
        // Invoke the constructor to create the instance
        try {
            final boolean setAccessible = !constructor.isAccessible();
            if (setAccessible) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(params);
            } finally {
                if (setAccessible) {
                    constructor.setAccessible(false);
                }
            }
        } catch (InstantiationException e) {
            throw new InjectException(e, ErrorStrings.FAILED_TO_INSTANTIATE_CLASS, mClassToInstantiate.getName());
        } catch (IllegalAccessException e) {
            throw new InjectException(e, ErrorStrings.FAILED_TO_INSTANTIATE_CLASS, mClassToInstantiate.getName());
        } catch (InvocationTargetException e) {
            throw new InjectException(e, ErrorStrings.FAILED_TO_INSTANTIATE_CLASS, mClassToInstantiate.getName());
        }
    }
}
