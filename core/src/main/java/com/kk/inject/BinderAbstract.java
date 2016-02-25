package com.kk.inject;

/**
 * Abstract binder.
 */
public abstract class BinderAbstract<T> implements Binder<T> {

    @NotNull protected final Factory mFactory;

    /**
     * Constructs the abstract binder.
     *
     * @param factory
     *         The factory which the binder is related to. Never {@code null}.
     */
    public BinderAbstract(@NotNull final Factory factory) {
        mFactory = factory;
    }
}
