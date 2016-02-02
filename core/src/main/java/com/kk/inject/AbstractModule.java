package com.kk.inject;

import com.kk.inject.internal.BindingId;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract module defines the bindings and performs the injections.
 */
public abstract class AbstractModule {

    private final Map<BindingId, Binding> mDefinedBindingMap;
    private final Map<BindingId, Method> mProducersMap;
    private final Factory mFactory;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Services
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Defines the bindings.
     */
    protected abstract void defineBindings();

    /**
     * Creates a binding.
     * <p/>
     * If there already is such a binding then it is silently overwritten.
     *
     * @param interfaceRequired
     *         The class or interface to bind to some implementation or instance. Never {@code null}.
     * @return The binding object ready for calling other methods detailing the binding strategy. Never {@code null}.
     */
    @NotNull
    protected <T> Binding<T> whenRequestedInstanceOf(@NotNull final Class<T> interfaceRequired) {
        return new Binding<T>(this, interfaceRequired);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Package protected
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs the module.
     * <p/>
     * Also prevents the direct instantiation (together with the abstraction).
     *
     * @param factory
     *         The factory to set to the module. Never {@code null}.
     */
    protected AbstractModule(@NotNull final Factory factory) {
        factory.registerModule(this);
        mFactory = factory;
        mDefinedBindingMap = new HashMap<BindingId, Binding>();
        mProducersMap = new HashMap<BindingId, Method>();
        collectSelfProducers();
        defineBindings();
    }

    /**
     * Retrieves the factory which this module is assigned to.
     *
     * @return The factory. Never {@code null}.
     */
    @NotNull
    Factory getFactory() {
        return mFactory;
    }

    /**
     * Adds the binding to the internal map.
     *
     * @param bindingId
     *         The binding ID for the binding. Never {@code null}.
     * @param binding
     *         The binding to add. Never {@code null}.
     */
    void addBinding(@NotNull final BindingId bindingId, @NotNull Binding<?> binding) {
        mDefinedBindingMap.put(bindingId, binding);
    }

    /**
     * Locates the binding for the specified class.
     *
     * @param bindingId
     *         The binding ID to locate the binding for. Never {@code null}.
     * @return The located binding. If no such class then {@code null}.
     */
    @Nullable
    <T> Binding<T> locateDefinedBinding(@NotNull final BindingId bindingId) {
        return mDefinedBindingMap.get(bindingId);
    }

    /**
     * Locates the producer method for the specified class.
     *
     * @param bindingId
     *         The binding ID to locate the producer method for. Never {@code null}.
     * @return The located producer method. If no such class then {@code null}.
     */
    @Nullable
    Method locateProducerMethod(@NotNull final BindingId bindingId) {
        return mProducersMap.get(bindingId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Collects producers for this module and adds them as a bindings.
     */
    private void collectSelfProducers() {
        // Setters injection
        final Method[] methods = getClass().getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Provides.class)) {
                String name = null;
                final Named namedAnnotation = method.getAnnotation(Named.class);
                if (namedAnnotation != null) {
                    name = namedAnnotation.value();
                }
                final BindingId bindingId = new BindingId(method.getReturnType(), name, null, null);
                mProducersMap.put(bindingId, method);
            }
        }
    }
}
