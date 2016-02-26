package com.kk.inject;

/**
 * Binder interface.
 * <p/>
 * It serves the instances for the single particular binding.
 *
 * @param <T>
 *         The class type to ensure the type safety by the compiler.
 */
abstract class Binder<T> {

    @NotNull protected final Factory mFactory;

    /**
     * Constructs the abstract binder.
     *
     * @param factory
     *         The factory which the binder is related to. Never {@code null}.
     */
    Binder(@NotNull final Factory factory) {
        mFactory = factory;
    }

    /**
     * Ensures the instance for the related binding.
     *
     * @param parameters
     *         The parameters to optionally pass to the newly created object. Can be missing. Some bindings can ignore
     *         it.
     * @return The ensured instance. Never {@code null}.
     * @throws InjectException
     *         When there is a problem with the instance ensuring. It is a runtime exception as such a problem mostly
     *         signals the clear programming error (like missing suitable constructor).
     */
    @NotNull
    abstract T get(@NotNull final Object... parameters);
}
