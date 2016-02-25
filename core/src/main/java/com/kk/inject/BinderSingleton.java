package com.kk.inject;

/**
 * Binding implementation: Singleton.
 */
class BinderSingleton<T> extends BinderAbstract<T> {

    @NotNull private final T mInstanceToReturn;

    /**
     * Constructs the binding.
     *
     * @param instanceToReturn
     *         The instance to return. Never {@code null}.
     */
    public BinderSingleton(@NotNull final Factory factory, @NotNull final T instanceToReturn) {
        super(factory);
        mInstanceToReturn = instanceToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(@NotNull final Object... parameters) {
        return mInstanceToReturn;
    }
}
