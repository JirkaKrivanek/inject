package com.kk.inject;

import com.kk.inject.internal.BindingId;
import com.kk.inject.internal.Constants;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class.
 */
public final class Factory {
    private static Factory sFactory;
    private final List<AbstractModule> mModules;
    private final Map<BindingId, Object> mSingletons;
    private final Map<Class<?>, Object> mGotObjects;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instantiation
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new factory object.
     *
     * @return The new factory object. Never {@code null}.
     */
    @NotNull
    public static Factory create() {
        return new Factory();
    }

    /**
     * Retrieves singleton factory object.
     *
     * @return The singleton factory object. Never {@code null}.
     */
    @NotNull
    public static synchronized Factory singleton() {
        if (sFactory == null) {
            sFactory = create();
        }
        return sFactory;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Injection
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Ensures the instance of the requested class or interface.
     *
     * @param clazz
     *         The requested class or interface. Never {@code null}.
     * @param parameters
     *         The parameters to be passed to the constructor. Can be empty (actually only rarely non-empty).
     * @param <T>
     *         The type for the strong type check.
     * @return The instance. Can be {@code null}.
     */
    @Nullable
    public synchronized <T> T get(@NotNull final Class<T> clazz, final Object... parameters) {
        mGotObjects.clear();
        try {
            T result = getInternal(new BindingId(clazz, null, null, null), parameters);
            injectAllGotObjects();
            return result;
        } finally {
            mGotObjects.clear();
        }
    }

    /**
     * Performs the injection on specified object manually.
     *
     * @param object
     *         The object to perform the injection on. Never {@code null}.
     * @param <T>
     *         The type of the object.
     * @return The reference to the injected object itself to be used for call chaining. Never {@code null}.
     */
    @NotNull
    public synchronized <T> T inject(@NotNull final T object) {
        inject(object.getClass(), null);
        return object;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Package protected
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Registers the specified module to this factory.
     *
     * @param module
     *         The module to register. Never {@code null}.
     */
    void registerModule(@NotNull final AbstractModule module) {
        mModules.add(module);
    }

    /**
     * Ensures the instance of the requested class or interface: Internal part.
     *
     * @param bindingId
     *         The binding ID to get the instance for. Never {@code null}.
     * @param parameters
     *         The parameters to be passed to the constructor. Can be empty.
     * @param <T>
     *         The type for the strong type check.
     * @return The instance. Can be {@code null}.
     */
    @Nullable
    <T> T getInternal(@NotNull final BindingId bindingId, final Object... parameters) {
        for (final AbstractModule module : mModules) {
            // First try producer methods
            final Method method = module.locateProducerMethod(bindingId);
            if (method != null) {
                T result = (T) invokeMethodWithParameters(module, method, null);
                mGotObjects.put(result.getClass(), result);
                return result;
            }
            // Then try bindings
            final Binding<T> binding = module.locateDefinedBinding(bindingId);
            if (binding != null) {
                T result = binding.get(parameters);
                mGotObjects.put(result.getClass(), result);
                return result;
            }
        }
        throw new InjectException(Constants.ERROR_UNKNOWN_BINDING + bindingId);
    }

    /**
     * Adds the singleton to the internal list.
     *
     * @param bindingId
     *         The binding ID under which to store the object. Never {@code null}.
     * @param object
     *         The singleton to store. Never {@code null}.
     */
    void addSingleton(@NotNull final BindingId bindingId, @NotNull final Object object) {
        mSingletons.put(bindingId, object);
    }

    /**
     * Retrieves the singleton from the internal list.
     *
     * @param bindingId
     *         The binding ID under which to look the object up. Never {@code null}.
     * @return The singleton or {@code null}.
     */
    @Nullable
    Object getSingleton(@NotNull final BindingId bindingId) {
        Object result = mSingletons.get(bindingId);
        if (result == null) {
            for (Object object : mSingletons.values()) {
                if (bindingId.getClazz().isAssignableFrom(object.getClass())) {
                    result = object;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Collects the parameter types first from the got objects map (if any) and then by regular injection.
     *
     * @param parametersTypes
     *         The array of required parameter types. If {@code null} or empty then zero length array is returned.
     * @param parametersAnnotations
     *         The annotations of the parameters. Never {@code null}.
     * @param gotObjects
     *         The map of objects to use to extract the parameters from. If {@code null} then only injection applies.
     * @return The parameters array. Never {@code null}.
     */
    @NotNull
    Object[] collectParametersToBeInjected(@Nullable final Class<?>[] parametersTypes, @Nullable final Annotation[][] parametersAnnotations, @Nullable final Map<Class, Object> gotObjects) {
        Object[] result;
        if (parametersTypes == null || parametersTypes.length <= 0) {
            // No parameter types
            result = new Object[0];
        } else {
            // Have some parameter types
            result = new Object[parametersTypes.length];
            for (int pi = 0; pi < parametersTypes.length; pi++) {
                // Check for name from annotation
                String name = null;
                final Annotation[] annotations = parametersAnnotations[pi];
                if (annotations != null && annotations.length > 0) {
                    for (final Annotation annotation : annotations) {
                        if (annotation instanceof Named) {
                            name = ((Named) annotation).value();
                            break;
                        }
                    }
                }

                // First look for the injections in the got classes
                final Class<?> parameterType = parametersTypes[pi];
                Object object = null;
                if (gotObjects != null) {
                    object = gotObjects.get(parameterType);
                }

                // Then try the regular injection
                if (object == null) {
                    // Collect annotation classes array for the binding ID
                    Class[] annotationClasses = null;
                    if (annotations != null && annotations.length > 0) {
                        annotationClasses = new Class[annotations.length];
                        for (int index = 0; index < annotations.length; index++) {
                            annotationClasses[index] = annotations[index].annotationType();
                        }
                    }
                    object = getInternal(new BindingId(parameterType, name, null, annotationClasses));
                }

                // Set to result
                result[pi] = object;
            }
        }
        return result;
    }

    /**
     * Invokes the method with collected parameters.
     *
     * @param object
     *         The object to invoke the method on. Never {@code null}.
     * @param method
     *         The method to invoke. Never {@code null}.
     * @param gotObjects
     *         The map of objects to use to extract the parameters from. If {@code null} then only injection applies.
     * @return The object returned by the method invocation. Can be {@code null}.
     */
    @Nullable
    Object invokeMethodWithParameters(@NotNull final Object object, @NotNull final Method method, @Nullable final Map<Class, Object> gotObjects) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        final Object[] parameters = collectParametersToBeInjected(parameterTypes, parametersAnnotations, gotObjects);
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            throw new InjectException(String.format(Constants.ERROR_COULD_NOT_CALL_INJECTION_METHOD, method.getName(), object.getClass().getName()), e);
        } catch (InvocationTargetException e) {
            throw new InjectException(String.format(Constants.ERROR_COULD_NOT_CALL_INJECTION_METHOD, method.getName(), object.getClass().getName()), e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs the factory object.
     */
    private Factory() {
        mModules = new ArrayList<AbstractModule>();
        mSingletons = new HashMap<BindingId, Object>();
        mGotObjects = new HashMap<Class<?>, Object>();
    }

    /**
     * Performs the injection into all got objects.
     */
    private void injectAllGotObjects() {
        final Map<Class, Object> allGotObjects = new HashMap<Class, Object>(mGotObjects);
        do {
            allGotObjects.putAll(mGotObjects);
            final Map<Class, Object> gotObjects = new HashMap<Class, Object>(mGotObjects);
            mGotObjects.clear();
            for (final Object object : gotObjects.values()) {
                inject(object, allGotObjects);
            }
        } while (!mGotObjects.isEmpty());
    }

    /**
     * Performs the injection into the specified object.
     *
     * @param object
     *         The object to inject to. Never {@code null}.
     * @param gotObjects
     *         The objects got during the previous injection. Can be {@code null}.
     */
    private void inject(@NotNull final Object object, @Nullable final Map<Class, Object> gotObjects) {
        // Fields injection
        final Field[] fields = object.getClass().getDeclaredFields();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                // Try to resolve the name
                String name = null;
                final Named namedAnnotation = field.getAnnotation(Named.class);
                if (namedAnnotation != null) {
                    name = namedAnnotation.value();
                }

                // Collect annotation classes array for the binding ID
                final Annotation[] annotations = field.getDeclaredAnnotations();
                Class[] annotationClasses = null;
                if (annotations != null && annotations.length > 0) {
                    annotationClasses = new Class[annotations.length];
                    for (int index = 0; index < annotations.length; index++) {
                        annotationClasses[index] = annotations[index].annotationType();
                    }
                }

                // First look for the injections in the got classes
                Class fieldClass = field.getType();
                Object objectToInject = gotObjects.get(fieldClass);
                if (objectToInject == null) {
                    // Then try the regular injection
                    objectToInject = getInternal(new BindingId(fieldClass, name, null, annotationClasses));
                }

                // Ensure access
                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                    } catch (final SecurityException e) {
                        throw new InjectException(String.format(Constants.ERROR_COULD_NOT_INJECT_FIELD, field.getName(), object.getClass().getName()), e);
                    }
                }

                // Set the object to the field
                try {
                    field.set(object, objectToInject);
                } catch (final IllegalAccessException e) {
                    throw new InjectException(String.format(Constants.ERROR_COULD_NOT_INJECT_FIELD, field.getName(), object.getClass().getName()), e);
                }
            }
        }

        // Setters injection
        final Method[] methods = object.getClass().getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Inject.class)) {
                invokeMethodWithParameters(object, method, gotObjects);
            }
        }
    }
}
