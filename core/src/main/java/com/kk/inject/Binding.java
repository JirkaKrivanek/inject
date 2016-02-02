package com.kk.inject;

import com.kk.inject.internal.BindingId;
import com.kk.inject.internal.Constants;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Binding defined by user in the {@link AbstractModule}.
 */
public final class Binding<T> {

    private final AbstractModule mModule;
    private final BindingId mBindingId;
    private Class<? extends T> mImplementation;
    private boolean mIsSingleton;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Miscellaneous
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs the binding.
     * <p/>
     * Constructor is intentionally package protected to avoid direct instantiation - use {@link
     * AbstractModule#whenRequestedInstanceOf(Class)} method instead.
     *
     * @param module
     *         The module which owns this binding. Never {@code null}.
     * @param anInterface
     *         The interface class of the binding. Never {@code null}.
     */
    Binding(@NotNull final AbstractModule module, @NotNull final Class<T> anInterface) {
        mModule = module;
        mBindingId = new BindingId(anInterface, null, null, null);
        mImplementation = null;
        mIsSingleton = anInterface.isAnnotationPresent(Singleton.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Definition
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds the condition to the binding: Only injections with the given name are matched.
     *
     * @param name
     *         The name to use. Never {@code null}.
     * @return Self reference to allow the builder like pattern. Never {@code null}.
     */
    @NotNull
    public Binding<T> ifNamed(@NotNull final String name) {
        mBindingId.setName(name);
        return this;
    }

    /**
     * Adds the condition to the binding: Only injections with the given annotation are matched.
     *
     * @param annotation
     *         The annotation to use. Never {@code null}.
     * @return Self reference to allow the builder like pattern. Never {@code null}.
     */
    @NotNull
    public Binding<T> ifAnnotatedWith(@NotNull final Class<? extends Annotation> annotation) {
        mBindingId.setAnnotation(annotation);
        return this;
    }

    /**
     * Marks this binding to produce the singleton.
     *
     * @return Self reference to allow the builder like pattern. Never {@code null}.
     */
    @NotNull
    public Binding<T> singleton() {
        mIsSingleton = true;
        return this;
    }

    /**
     * Defines a binding to the specified class.
     * <p/>
     * If the class is annotated as singleton then the flag is set automatically (no need to call the {@link
     * #singleton()} method).
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Must be last in the builder chain!</dd></dl>
     *
     * @param implementation
     *         The class to bind to the binding. Never {@code null}.
     */
    public void thenInstantiate(@NotNull final Class<? extends T> implementation) {
        if (implementation.isAnnotationPresent(Singleton.class)) {
            mIsSingleton = true;
        }
        mImplementation = implementation;
        mModule.addBinding(mBindingId, this);
    }

    /**
     * Defines a binding to the specified instance.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Must be last in the builder chain!</dd></dl>
     *
     * @param instance
     *         The class to bind to the binding. Can be {@code null}.
     */
    public void thenReturn(@Nullable final T instance) {
        mIsSingleton = true;
        mModule.addBinding(mBindingId, this);
        mModule.getFactory().addSingleton(mBindingId, instance);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Instantiation
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the instance.
     *
     * @param parameters
     *         The parameters to be passed to the constructor. Can be empty (actually only rarely non-empty)..
     * @return The requested instance. Can be {@code null}.
     */
    @Nullable
    T get(final Object... parameters) {
        T instance;
        if (mIsSingleton) {
            instance = (T) mModule.getFactory().getSingleton(mBindingId);
            if (instance != null) {
                return instance;
            }
        }
        instance = instantiate(parameters);
        if (mIsSingleton) {
            mModule.getFactory().addSingleton(mBindingId, instance);
        }
        return instance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates the instance of the implementation class and performs the first round of the injections.
     *
     * @param parameters
     *         The parameters to be passed to the constructor. Can be empty.
     * @return The created instance. Can be {@code null}.
     */
    private T instantiate(Object... parameters) {
        // Validate
        if (mImplementation == null) {
            throw new InjectException(String.format(Constants.ERROR_INCOMPLETE_BINDING, mBindingId.getClazz().getName()));
        }

        // Locate constructor and ensure parameters
        Constructor<? extends T> constructor = null;
        final Constructor<? extends T>[] constructors = (Constructor<? extends T>[]) mImplementation.getConstructors();
        if (constructors != null && constructors.length > 0) {
            // Have at least some constructor
            if (parameters != null && parameters.length > 0) {
                // Try to locate constructor with the specified parameters
                for (final Constructor<? extends T> constructorIter : constructors) {
                    final Class<?>[] parameterTypes = constructorIter.getParameterTypes();
                    if (parameterTypes != null && parameterTypes.length == parameters.length) {
                        boolean match = true;
                        for (int pi = 0; pi < parameters.length; pi++) {
                            final Object parameter = parameters[pi];
                            if (parameter != null) {
                                final Class<?> parameterType = parameterTypes[pi];
                                if (!parameterType.isAssignableFrom(parameter.getClass())) {
                                    match = false;
                                    break;
                                }
                            }
                        }
                        if (match) {
                            constructor = constructorIter;
                            break;
                        }
                    }
                }
            } else {
                // No specified parameters: Try to locate first constructor with the Inject annotation
                for (final Constructor<? extends T> constructorIter : constructors) {
                    if (constructorIter.isAnnotationPresent(Inject.class)) {
                        constructor = constructorIter;
                        // Collect parameters (if any) - so nested injection applies
                        final Class<?>[] parameterTypes = constructor.getParameterTypes();
                        final Annotation[][] parametersAnnotations = constructor.getParameterAnnotations();
                        parameters = mModule.getFactory().collectParametersToBeInjected(parameterTypes, parametersAnnotations, null);
                        break;
                    }
                }
                // If constructor not found then try to use the default one
                if (constructor == null) {
                    for (final Constructor<? extends T> constructorIter : constructors) {
                        final Class<?>[] parameterTypes = constructorIter.getParameterTypes();
                        if (parameterTypes == null || parameterTypes.length <= 0) {
                            constructor = constructorIter;
                            parameters = new Object[0];
                            break;
                        }
                    }
                }
            }
        }

        // Create the instance
        if (constructor == null) {
            throw new InjectException(String.format(Constants.ERROR_MISSING_CONSTRUCTOR, mImplementation.getName()));
        }
        try {
            return constructor.newInstance(parameters);
        } catch (final InstantiationException e) {
            throw new InjectException(String.format(Constants.ERROR_COULD_NOT_CREATE_INSTANCE, mImplementation.getName()), e);
        } catch (final IllegalAccessException e) {
            throw new InjectException(String.format(Constants.ERROR_COULD_NOT_CREATE_INSTANCE, mImplementation.getName()), e);
        } catch (final InvocationTargetException e) {
            throw new InjectException(String.format(Constants.ERROR_COULD_NOT_CREATE_INSTANCE, mImplementation.getName()), e);
        }
    }
}
