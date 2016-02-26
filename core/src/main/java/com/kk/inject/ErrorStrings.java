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

    static final String CANNOT_MANAGER_FACTORY_WHEN_INJECTING = "Cannot manage factory when injecting";
    static final String COULD_NOT_CONSTRUCT_MODULE_OBJECT     = "Could not construct module object";
    static final String NO_SUITABLE_CONSTRUCTOR               = "No suitable constructor found to instantiate class %s";
    static final String FAILED_TO_INSTANTIATE_CLASS           = "Failed to instantiate class %s";
    static final String NO_BINDER                             = "No binder for %s";
    static final String FAILED_TO_INJECT_FIELD                = "Failed to inject field %s of class %s";
    static final String FAILED_TO_INJECT_METHOD               = "Failed to inject method %s of class %s";
    static final String PROVIDER_MUST_HAVE_METHOD             = "Missing or inaccessible method %s on provider %s";
    static final String PROVIDER_MUST_NOT_RETURN_VOID         = "Provider %s of module %s returns void";
    static final String PROVIDER_PARAMETERS_MISMATCH          = "Provider %s of module %s has different parameters than supplied";
    static final String FAILED_TO_CALL_PROVIDER               = "Failed to call the provider method %s on module %s";
}
