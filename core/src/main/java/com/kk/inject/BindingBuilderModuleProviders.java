package com.kk.inject;

/**
 * Binding builder: Module provider.
 */
final class BindingBuilderModuleProviders {

    @NotNull private final Factory        mFactory;
    @NotNull private final AbstractModule mModule;

    /**
     * Constructs the builder of the bindings by the module providers.
     *
     * @param factory
     *         The factory which the bindings will belong to. Never {@code null}.
     * @param module
     *         The module to scan for the provided bindings. Never {@code null}.
     */
    BindingBuilderModuleProviders(@NotNull final Factory factory, @NotNull final AbstractModule module) {
        mFactory = factory;
        mModule = module;
    }

    /**
     * Builds the bindings.
     */
    void build() {
        // TODO: Support for provided bindings
    }
}
