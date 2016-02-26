package com.kk.inject;

/**
 * Binding implementation: Singleton.
 */
class BinderSingleton<T> extends Binder<T> {

    @NotNull private final T mInstanceToReturn;

    /**
     * Constructs the binding.
     *
     * @param instanceToReturn
     *         The instance to return. Never {@code null}.
     */
    BinderSingleton(@NotNull final Factory factory, @NotNull final T instanceToReturn) {
        super(factory);
        mInstanceToReturn = instanceToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    T get(@NotNull final Object... parameters) {
        return mInstanceToReturn;
    }
}
