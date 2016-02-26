package com.kk.inject;

import java.lang.annotation.Annotation;

/**
 * Utilities.
 */
final class Utils {

    /**
     * Prevent instantiation.
     */
    private Utils() {
    }

    /**
     * Checks whether the parameters match the types.
     *
     * @param requiredTypes
     *         The types to check. Can be {@code null}.
     * @param parameters
     *         The parameters to check. Can be {@code null}.
     * @return If matching then {@code true} else {@code false}.
     */
    static boolean checkParameterTypes(@NotNull final Class<?>[] requiredTypes, @NotNull final Object[] parameters) {
        if (parameters == null || parameters.length <= 0) {
            return requiredTypes == null || requiredTypes.length <= 0;
        }
        if (requiredTypes == null || requiredTypes.length != parameters.length) {
            return false;
        }
        for (int index = 0; index < parameters.length; index++) {
            final Class<?> requiredType = requiredTypes[index];
            final Object parameter = parameters[index];
            if (!checkParameterType(requiredType, parameter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the parameter match the type.
     *
     * @param requiredType
     *         The type to check. Can be {@code null}.
     * @param parameter
     *         The parameter to check. Can be {@code null}.
     * @return If matching then {@code true} else {@code false}.
     */
    static boolean checkParameterType(@NotNull final Class<?> requiredType, @NotNull final Object parameter) {
        if (parameter == null) {
            return !requiredType.isPrimitive();
        }
        if (requiredType.isPrimitive()) {
            return requiredType.getName().equals("byte") && parameter instanceof Byte ||
                    requiredType.getName().equals("short") && parameter instanceof Short ||
                    requiredType.getName().equals("int") && parameter instanceof Integer ||
                    requiredType.getName().equals("long") && parameter instanceof Long ||
                    requiredType.getName().equals("float") && parameter instanceof Float ||
                    requiredType.getName().equals("double") && parameter instanceof Double ||
                    requiredType.getName().equals("boolean") && parameter instanceof Boolean ||
                    requiredType.getName().equals("char") && parameter instanceof Character;
        }
        return requiredType.isAssignableFrom(parameter.getClass());
    }

    /**
     * Extracts the name from the annotations list.
     *
     * @param annotations
     *         The annotations list to check. If {@code null} then no annotations to be processed.
     * @return If there is the {@link Named} annotation then the name else {@code null}.
     */
    @Nullable
    static String extractNameFromAnnotations(@Nullable final Annotation[] annotations) {
        if (annotations != null) {
            for (final Annotation annotation : annotations) {
                if (annotation instanceof Named) {
                    return ((Named) annotation).value();
                }
            }
        }
        return null;
    }

    /**
     * Checks whether to use the annotation for the binding definition or not.
     *
     * @param annotation
     *         The annotation to check. Can be {@code null}.
     * @return If annotation shall be used then {@code true} else {@code false}.
     */
    static boolean useAnnotationForBinding(@Nullable final Annotation annotation) {
        return annotation != null && !(annotation instanceof Named) && !(annotation instanceof Inject) &&
                !(annotation instanceof Singleton) && !(annotation instanceof NotNull) &&
                !(annotation instanceof Nullable);
    }
}
