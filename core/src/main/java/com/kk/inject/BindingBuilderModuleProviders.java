package com.kk.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Binding builder: Module provider.
 */
final class BindingBuilderModuleProviders {

    @NotNull private final Factory mFactory;
    @NotNull private final Object  mObject;

    /**
     * Constructs the builder of the bindings by the object providers.
     *
     * @param factory
     *         The factory which the bindings will belong to. Never {@code null}.
     * @param object
     *         The object to scan for the provided bindings. Never {@code null}.
     */
    BindingBuilderModuleProviders(@NotNull final Factory factory, @NotNull final Object object) {
        mFactory = factory;
        mObject = object;
    }

    /**
     * Builds the bindings.
     */
    void build() {
        // Walk through all methods and filter only those annotated as providers
        final Method[] methods = mObject.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Provides.class)) {
                buildBinder(method);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internals
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates one or more bindings for the provider method.
     *
     * @param method
     *         The method for which to create the binding(s). Never {@code null}.
     */
    private void buildBinder(@NotNull final Method method) {
        final Class<?> forClass = method.getReturnType();
        // Check provider does not return void
        if (forClass == null || forClass.equals(Void.TYPE)) {
            throw new InjectException(ErrorStrings.PROVIDER_MUST_NOT_RETURN_VOID,
                                      method.getName(),
                                      mObject.getClass().getName());
        }
        // Create the binder object to be assigned to the factory
        final Binder binder = new BinderProvider(mFactory, mObject, method);
        // Retrieve the annotations for further processing
        final Annotation[] annotations = method.getAnnotations();
        // If have some annotations then create multiple bindings otherwise just one
        boolean needSimpleBinding = true;
        String name = null;
        if (annotations != null && annotations.length > 0) {
            // Find the name (if any such annotation)
            name = Utils.extractNameFromAnnotations(annotations);
            // Go through the annotations and create the bindings
            for (final Annotation annotation : annotations) {
                if (Utils.useAnnotationForBinding(annotation) && !(annotation instanceof Provides)) {
                    final BindingId bindingId = new BindingId(forClass, name, annotation.annotationType());
                    mFactory.addBinding(bindingId, binder);
                    needSimpleBinding = false;
                }
            }
        }
        if (needSimpleBinding) {
            final BindingId bindingId = new BindingId(forClass, name, null);
            mFactory.addBinding(bindingId, binder);
        }
    }
}
