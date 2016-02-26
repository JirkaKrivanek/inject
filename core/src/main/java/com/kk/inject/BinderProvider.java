package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Binding implementation: Module provider.
 */
class BinderProvider<T> extends Binder<T> {

    @NotNull private final Object mObject;
    @NotNull private final Method mMethod;

    /**
     * Constructs the binding.
     *
     * @param factory
     *         The factory which the bindings will belong to. Never {@code null}.
     * @param object
     *         The object which implements the method to invoke in order to obtain the instance. Never {@code null}.
     * @param method
     *         The method to invoke in order to obtain the instance. Never {@code null}.
     */
    BinderProvider(@NotNull final Factory factory, @NotNull Object object, @NotNull final Method method) {
        super(factory);
        mObject = object;
        mMethod = method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    T get(@NotNull final Object... parameters) {
        if (parameters.length <= 0) {
            return instantiateWithInjection();
        } else {
            return instantiateWithParameters(parameters);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates the class using the module provider method with the parameters given ba the caller.
     *
     * @param parameters
     *         The parameters list which are checked against the method and then passed to it.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T instantiateWithParameters(@NotNull final Object... parameters) {
        // Check parameter types
        final Class<?>[] parameterTypes = mMethod.getParameterTypes();
        if (!Utils.checkParameterTypes(parameterTypes, parameters)) {
            throw new InjectException(ErrorStrings.PROVIDER_PARAMETERS_MISMATCH,
                                      mMethod.getName(),
                                      mObject.getClass().getName());
        }
        // Invoke the provider
        return invokeProvider(parameters);
    }

    /**
     * Instantiates the class using the module provider method call with the parameters collected by the injection.
     *
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    private T instantiateWithInjection() {
        // Prepare the parameters
        final Class<?>[] parameterTypes = mMethod.getParameterTypes();
        final Annotation[][] parameterAnnotations = mMethod.getParameterAnnotations();
        final Object[] parameters = mFactory.collectParametersToInject(parameterTypes, parameterAnnotations);
        // Invoke the provider
        return invokeProvider(parameters);
    }

    /**
     * Invokes the specified module provider method passing the specified parameters list to it.
     *
     * @param parameters
     *         The list of parameters to pass to the module provider method. If {@code null} then NO parameters method
     *         is expected.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private T invokeProvider(@NotNull final Object... parameters) {
        try {
            final boolean setAccessible = !mMethod.isAccessible();
            if (setAccessible) {
                mMethod.setAccessible(true);
            }
            try {
                return (T) mMethod.invoke(mObject, parameters);
            } finally {
                if (setAccessible) {
                    mMethod.setAccessible(false);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InjectException(e,
                                      ErrorStrings.FAILED_TO_CALL_PROVIDER,
                                      mMethod.getName(),
                                      mObject.getClass().getName());
        }
    }
}
