package com.kk.inject;

/**
 * Injection exception.
 */
public final class InjectException extends RuntimeException {

    /**
     * Constructs the exception with the detailed message.
     *
     * @param detailedMessage
     *         The detailed message. Can be {@code null}.
     */
    public InjectException(@Nullable final String detailedMessage) {
        super(detailedMessage);
    }

    /**
     * Constructs the exception with the detailed message.
     *
     * @param detailedMessage
     *         The detailed message. Can be {@code null}.
     * @param cause
     *         The cause for this exception. Can be {@code null}.
     */
    public InjectException(@Nullable final String detailedMessage, @Nullable final Throwable cause) {
        super(detailedMessage, cause);
    }
}
