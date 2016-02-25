package com.kk.inject;

/**
 * Error strings for the exceptions.
 */
final class ErrorStrings {

    /**
     * Prevents instantiation.
     */
    private ErrorStrings() {
    }

    static final String COULD_NOT_CONSTRUCT_MODULE_OBJECT = "Could not construct module object";
    static final String NO_SUITABLE_CONSTRUCTOR           = "No suitable constructor found to instantiate class %s";
    static final String FAILED_TO_INSTANTIATE_CLASS       = "Failed to instantiate class %s";
    static final String NO_BINDER                         = "No binder for %s";
    static final String FAILED_TO_INJECT_FIELD            = "Failed to inject field %s of class %s";
    static final String FAILED_TO_INJECT_METHOD           = "Failed to inject method %s of class %s";
}
