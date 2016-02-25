package com.kk.inject;

/**
 * Injection exception.
 */
public final class InjectException extends RuntimeException {

    /**
     * Constructs the exception with the formatted detailed message.
     * <p/>
     * See {@link String@format} method.
     *
     * @param format
     *         The detailed message format. Never {@code null}.
     * @param args
     *         The format arguments.
     */
    public InjectException(@NotNull final String format, final Object... args) {
        super(String.format(format, args));
    }

    /**
     * Constructs the exception with the formatted detailed message and cause.
     *
     * @param cause
     *         The cause for this exception. Can be {@code null}.
     * @param format
     *         The detailed message format. Never {@code null}.
     * @param args
     *         The format arguments.
     */
    public InjectException(@Nullable final Throwable cause, @NotNull final String format, final Object... args) {
        super(String.format(format, args), cause);
    }
}
