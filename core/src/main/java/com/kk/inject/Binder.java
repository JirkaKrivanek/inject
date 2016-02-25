package com.kk.inject;

/**
 * Binding interface.
 * <p/>
 * It serves the instances for the single particular binding.
 *
 * @param <T>
 *         The class type to ensure the type safety by the compiler.
 */
interface Binder<T> {

    /**
     * Ensures the instance for the related binding.
     *
     * @param parameters
     *         The parameters to optionally pass to the newly created object. Can be missing. Some bindings can ignore
     *         it.
     * @return The ensured instance. Never {@code null}.
     */
    @NotNull
    T get(@NotNull final Object... parameters);
}
