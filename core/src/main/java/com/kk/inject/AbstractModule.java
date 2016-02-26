package com.kk.inject;

/**
 * The abstract module to be subclassed and used as instances ensurer.
 */
public abstract class AbstractModule {

    /**
     * Filled in by the factory when registering the module to the factory.
     * <p/>
     * So that the binding builders and the providers can use the factory for its operation too.
     */
    @Nullable private Factory mFactory;

    /**
     * Retrieves the factory.
     * <p/>
     * This method is available to all subclasses only.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Please be aware that the factory is null intin the module is registered with
     * the factory!</dd></dl>
     *
     * @return The factory.
     */
    protected Factory getFactory() {
        return mFactory;
    }

    /**
     * Sets the factory.
     * <p/>
     * This method is only available for the package - so it is internal to prevent misuse by the applications.
     *
     * @param factory
     *         The factory to set. Never {@code null}.
     */
    void setFactory(@NotNull final Factory factory) {
        mFactory = factory;
    }

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
