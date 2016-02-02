package com.kk.inject.internal;

/**
 * Constant for the injection.
 */
public final class Constants {
    public static final String ERROR_MODULE_FACTORY_MISMATCH = "Module can only be registered to single factory";
    public static final String ERROR_UNKNOWN_BINDING = "Unknown binding: ";
    public static final String ERROR_INCOMPLETE_BINDING = "Binding is missing implementation for class: %s";
    public static final String ERROR_MISSING_CONSTRUCTOR = "No suitable constructor found in class: %s";
    public static final String ERROR_COULD_NOT_CREATE_INSTANCE = "Could not create instance of class: %s";
    public static final String ERROR_COULD_NOT_INJECT_FIELD = "Could not inject field \"%s\" of class: %s";
    public static final String ERROR_COULD_NOT_CALL_INJECTION_METHOD = "Could not call injection method \"%s\" on class: %s";
    public static final String ERROR_BINDING_ALREADY_USED = "Cannot change binding as it is already used";
    public static final String ERROR_BINDING_ID_CANNOT_HAVE_BOTH_ANNOTATIONS = "Binding ID cannot have both the annotation and the annotations";
}
