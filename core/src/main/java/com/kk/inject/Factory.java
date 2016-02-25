package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for the dependency injection.
 */
public final class Factory {

    @Nullable private static Factory                               sFactory;
    @Nullable private static List<Class<? extends AbstractModule>> sModuleClasses;

    @NotNull private final Map<BindingId, Binder> mBindings;
    @NotNull private final List<Object>           mObjectsToInject;
    @NotNull private final List<Object>           mInjectedObjects;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factory API: Factory definition
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Resets the factory definition.
     */
    public static synchronized void resetFactoryDefinition() {
        if (sModuleClasses != null) {
            sModuleClasses.clear();
        }
    }

    /**
     * Adds the module class to the definition list of the factory class.
     *
     * @param moduleClass
     *         The class of the module to register. Never {@code null}.
     */
    public static synchronized void addModuleClass(@NotNull final Class<? extends AbstractModule> moduleClass) {
        if (sModuleClasses == null) {
            sModuleClasses = new ArrayList<>();
        }
        sModuleClasses.add(moduleClass);
    }

    /**
     * Creates the factory as defined by the methods above.
     *
     * @return The factory. Never {@code null}.
     */
    @NotNull
    public static synchronized Factory createFactory() {
        final Factory factory = new Factory();
        if (sModuleClasses != null) {
            for (final Class<? extends AbstractModule> moduleClass : sModuleClasses) {
                AbstractModule module;
                try {
                    final Constructor<? extends AbstractModule> constructor = moduleClass.getConstructor();
                    final boolean setAccessible = !constructor.isAccessible();
                    if (setAccessible) {
                        constructor.setAccessible(true);
                    }
                    try {
                        module = constructor.newInstance();
                    } finally {
                        if (setAccessible) {
                            constructor.setAccessible(false);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    throw new InjectException(ErrorStrings.COULD_NOT_CONSTRUCT_MODULE_OBJECT, e);
                } catch (InvocationTargetException e) {
                    throw new InjectException(ErrorStrings.COULD_NOT_CONSTRUCT_MODULE_OBJECT, e);
                } catch (InstantiationException e) {
                    throw new InjectException(ErrorStrings.COULD_NOT_CONSTRUCT_MODULE_OBJECT, e);
                } catch (IllegalAccessException e) {
                    throw new InjectException(ErrorStrings.COULD_NOT_CONSTRUCT_MODULE_OBJECT, e);
                }
                factory.register(module);
            }
        }
        return factory;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factory API: Singleton variant
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Resets the singleton instance of the factory.
     */
    public static synchronized void resetSingletonFactory() {
        if (sFactory != null) {
            sFactory.reset();
            sFactory = null;
        }
    }

    /**
     * Registers the module object to the singleton factory instance.
     *
     * @param module
     *         The class of the module to register. Never {@code null}.
     */
    public static synchronized void registerModule(@NotNull final AbstractModule module) {
        getFactorySingleton().register(module);
    }

    /**
     * Ensures the instance of the specified class using the registered modules.
     *
     * @param clazz
     *         The class to instantiate. Never {@code null}.
     * @param parameters
     *         The optional parameters to be passed to the newly created instance. Can be missing.
     * @param <T>
     *         The class type to ensure the type safety by the compiler.
     * @return The ensured instance. Never {@code null}.
     */
    @NotNull
    public static synchronized <T> T getInstance(@NotNull final Class<T> clazz, @NotNull final Object... parameters) {
        return getFactorySingleton().get(clazz, parameters);
    }

    /**
     * Performs the injection on the object created other way than {@link #getInstance(Class, Object...)}.
     *
     * @param objectToInject
     *         The object to perform the injection on. Never {@code null}.
     * @param <T>
     *         The class type to ensure the type safety by the compiler.
     * @return The object with injection performed. Never {@code null}.
     */
    @NotNull
    public static synchronized <T> T injectInstance(@NotNull final T objectToInject) {
        return getFactorySingleton().inject(objectToInject);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Factory API: Instance variant
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Resets the factory.
     */
    public synchronized void reset() {
        mBindings.clear();
        mObjectsToInject.clear();
        mInjectedObjects.clear();
    }

    /**
     * Registers the module to the factory instance.
     *
     * @param module
     *         The class of the module to register. Never {@code null}.
     */
    public synchronized void register(@NotNull final AbstractModule module) {
        module.mFactory = this;
        try {
            module.defineBindings();
            new BindingBuilderModuleProviders(this, module).build();
        } finally {
            module.mFactory = null;
        }
    }

    /**
     * Ensures the instance of the specified class using the registered modules.
     *
     * @param clazz
     *         The class to instantiate. Never {@code null}.
     * @param parameters
     *         The optional parameters to be passed to the newly created instance. Can be missing.
     * @param <T>
     *         The class type to ensure the type safety by the compiler.
     * @return The ensured instance. Never {@code null}.
     */
    @NotNull
    public synchronized <T> T get(@NotNull final Class<T> clazz, @NotNull final Object... parameters) {
        final BindingId bindingId = new BindingId(clazz, null, null);
        final Binder<T> binder = locateBinder(bindingId, true);
        mInjectedObjects.clear();
        mObjectsToInject.clear();
        try {
            T result = binder.get(parameters);
            injectAll();
            return result;
        } finally {
            mInjectedObjects.clear();
            mObjectsToInject.clear();
        }
    }

    /**
     * Performs the injection on the object created other way than {@link #getInstance(Class, Object...)}.
     *
     * @param objectToInject
     *         The object to perform the injection on. Never {@code null}.
     * @param <T>
     *         The class type to ensure the type safety by the compiler.
     * @return The object with injection performed. Never {@code null}.
     */
    @NotNull
    public synchronized <T> T inject(@NotNull final T objectToInject) {
        mInjectedObjects.clear();
        mObjectsToInject.clear();
        try {
            mObjectsToInject.add(objectToInject);
            injectAll();
            return objectToInject;
        } finally {
            mInjectedObjects.clear();
            mObjectsToInject.clear();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals: Class
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Ensures the factory singleton for the simplified usage.
     *
     * @return The factory singleton. Never {@code null}.
     */
    @NotNull
    private static synchronized Factory getFactorySingleton() {
        if (sFactory == null) {
            sFactory = createFactory();
        }
        return sFactory;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Package internals
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds binding. If such a binding already exists then silently replaces it.
     *
     * @param bindingId
     *         The binding ID. Never {@code null}.
     * @param binding
     *         The binding implementation. Never {@code null}.
     */
    void addBinding(@NotNull final BindingId bindingId, @NotNull final Binder binding) {
        mBindings.put(bindingId, binding);
    }

    /**
     * Collects the array of parameters for constructor/method invocation by injection from types and annotations.
     *
     * @param parameterTypes
     *         The required parameters types. If {@code null} then no parameters.
     * @param parametersAnnotations
     *         The optional annotations detailing the injection process. Can be {@code null}.
     * @return The array of injected parameters. If no parameters ({@code parameterTypes} is null or zero length) then
     * {@code null}.
     */
    @Nullable
    Object[] collectParametersToInject(@Nullable final Class<?>[] parameterTypes,
                                       @Nullable final Annotation[][] parametersAnnotations) {
        if (parameterTypes != null && parameterTypes.length > 0) {
            final Object[] result = new Object[parameterTypes.length];
            for (int index = 0; index < parameterTypes.length; index++) {
                final Class<?> parameterType = parameterTypes[index];
                final Annotation[] parameterAnnotations;
                if (parametersAnnotations == null) {
                    parameterAnnotations = null;
                } else {
                    parameterAnnotations = parametersAnnotations[index];
                }
                result[index] = collectParameterToInject(parameterType, parameterAnnotations);
            }
            return result;
        }
        return null;
    }

    /**
     * Collects the parameter for constructor/method/field injection from types and annotations.
     *
     * @param parameterType
     *         The required parameter type. Never {@code null}.
     * @param parameterAnnotations
     *         The optional annotations detailing the injection process. Can be {@code null}.
     * @return The injected parameters. Never {@code null}.
     */
    @NotNull
    Object collectParameterToInject(@NotNull final Class<?> parameterType,
                                    @Nullable final Annotation[] parameterAnnotations) {
        // Shall consider annotations?
        String name = null;
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            // Extract name from the annotation (if any)
            for (final Annotation annotation : parameterAnnotations) {
                if (annotation.annotationType().equals(Named.class)) {
                    name = ((Named) annotation).value();
                }
            }
            // Go through all other annotations and try to locate the binding for it
            for (final Annotation annotation : parameterAnnotations) {
                if (annotation.annotationType().equals(Named.class)) {
                    continue;
                }
                if (annotation.annotationType().equals(Inject.class)) {
                    continue;
                }
                final BindingId bindingId = new BindingId(parameterType, name, annotation);
                final Binder binder = locateBinder(bindingId, false);
                if (binder != null) {
                    return binder.get();
                }
            }
        }
        // Try binding without any annotations
        final BindingId bindingId = new BindingId(parameterType, name, null);
        final Binder binder = locateBinder(bindingId, true);
        return binder.get();
    }

    /**
     * Records object to be injects (its annotated methods and fields).
     *
     * @param objectToInject
     *         The object to be injected. Never {@code null}.
     */
    void injectObject(@NotNull Object objectToInject) {
        for (final Object object : mObjectsToInject) {
            if (objectToInject == object) {
                return;
            }
        }
        for (final Object object : mInjectedObjects) {
            if (objectToInject == object) {
                return;
            }
        }
        mObjectsToInject.add(objectToInject);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals: Instance
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Prevents direct instantiation.
     */
    private Factory() {
        mBindings = new HashMap<>();
        mObjectsToInject = new ArrayList<>();
        mInjectedObjects = new ArrayList<>();
    }

    /**
     * Locates the binder for the specified binding ID.
     *
     * @param bindingId
     *         The binding ID to locate. Never {@code null}.
     * @param throwException
     *         Pass {@code true} to throw exception when no binding found or {@code false} to just return {@code null}.
     * @param <T>
     *         The class type to ensure the type safety by the compiler.
     * @return The located binder. If not throwing exceptions then may be {@code null}.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    private <T> Binder<T> locateBinder(@NotNull final BindingId bindingId, final boolean throwException) {
        final Binder<T> result = mBindings.get(bindingId);
        if (result == null) {
            throw new InjectException(ErrorStrings.NO_BINDER, bindingId.toString());
        }
        return result;
    }

    /**
     * Injects all object in the {@link #mObjectsToInject} list.
     */
    private void injectAll() {
        while (!mObjectsToInject.isEmpty()) {
            Object objectToInject = mObjectsToInject.remove(mObjectsToInject.size() - 1);
            mInjectedObjects.add(objectToInject);
            injectOne(objectToInject);
        }
    }

    /**
     * Injects single object.
     *
     * @param objectToInject
     *         The object onto which to perform the injection.
     */
    private void injectOne(@NotNull Object objectToInject) {
        // Inject fields
        injectOneFields(objectToInject, objectToInject.getClass().getDeclaredFields());
        // Inject setter methods
        injectOneMethods(objectToInject, objectToInject.getClass().getDeclaredMethods());
    }

    /**
     * Injects fields of single object.
     *
     * @param objectToInject
     *         The object onto which to perform the injection.
     * @param fields
     *         The fields to consider for injecting.
     */
    private void injectOneFields(@NotNull Object objectToInject, final Field[] fields) {
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectOneField(objectToInject, field);
            }
        }
    }

    /**
     * Injects one field of single object.
     *
     * @param objectToInject
     *         The object onto which to perform the injection.
     * @param field
     *         The field to inject.
     */
    private void injectOneField(@NotNull Object objectToInject, final Field field) {
        final Class<?> type = field.getType();
        final Annotation[] annotations = field.getDeclaredAnnotations();
        final Object value = collectParameterToInject(type, annotations);
        final boolean setAccessible = !field.isAccessible();
        if (setAccessible) {
            field.setAccessible(true);
        }
        try {
            try {
                field.set(objectToInject, value);
            } catch (IllegalAccessException e) {
                throw new InjectException(ErrorStrings.FAILED_TO_INJECT_FIELD, field.getName(), type.getName());
            }
        } finally {
            if (setAccessible) {
                field.setAccessible(false);
            }
        }
    }

    /**
     * Injects methods of single object.
     *
     * @param objectToInject
     *         The object onto which to perform the injection.
     * @param methods
     *         The methods to consider for injecting.
     */
    private void injectOneMethods(@NotNull Object objectToInject, final Method[] methods) {
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Inject.class)) {
                injectOneMethod(objectToInject, method);
            }
        }
    }

    /**
     * Injects one method of single object.
     *
     * @param objectToInject
     *         The object onto which to perform the injection.
     * @param method
     *         The method to inject.
     */
    private void injectOneMethod(@NotNull Object objectToInject, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final Object[] params = collectParametersToInject(parameterTypes, parameterAnnotations);
        try {
            final boolean setAccessible = !method.isAccessible();
            if (setAccessible) {
                method.setAccessible(true);
            }
            try {
                method.invoke(objectToInject, params);
            } finally {
                if (setAccessible) {
                    method.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            throw new InjectException(e,
                                      ErrorStrings.FAILED_TO_INJECT_METHOD,
                                      method.getName(),
                                      objectToInject.getClass().getName());
        } catch (InvocationTargetException e) {
            throw new InjectException(e,
                                      ErrorStrings.FAILED_TO_INJECT_METHOD,
                                      method.getName(),
                                      objectToInject.getClass().getName());
        }
        // TODO: Test annotated injections into: constructors
        // TODO: Test annotated injections into: fields
        // TODO: Test annotated injections into: methods
    }
}
