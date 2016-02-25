package com.kk.inject;

/**
 * The abstract module to be subclassed and used as instances ensurer.
 */
public abstract class AbstractModule {

    /**
     * Filled in by the factory when registering the module and cleared out after that immediately.
     * <p/>
     * So that the binding builders can use the factory for its operation.
     */
    @Nullable Factory mFactory;

    /**
     * Defines the bindings.
     */
    protected abstract void defineBindings();

    /**
     * Creates the builder to define the binding.
     *
     * @param forClass
     *         The class for which the instance is requested. Never {@code null}.
     * @return The builder. Never {@code null}.
     */
    @NotNull
    protected <T> BindingBuilderManual<T> whenRequestedInstanceOf(@NotNull final Class<T> forClass) {
        return new BindingBuilderManual<>(mFactory, forClass);
    }
}
