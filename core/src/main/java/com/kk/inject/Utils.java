package com.kk.inject;

/**
 * Utilities.
 */
final class Utils {

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
            if (parameter == null) {
                if (requiredType.isPrimitive()) {
                    return false;
                }
            } else {
                final Class<?> parameterType = parameter.getClass();
                if (!requiredType.isAssignableFrom(parameterType)) {
                    return false;
                }
            }
        }
        return true;
    }
}
