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
        return checkType(parameter.getClass(), requiredType);
    }

    /**
     * Checks whether the type A can be assigned to type B.
     * <p/>
     * It handles the primitive types properly.
     *
     * @param typeA
     *         The type A to check. Can be {@code null}.
     * @param typeB
     *         The type B to check. Can be {@code null}.
     * @return If matching then {@code true} else {@code false}.
     */
    static boolean checkType(@NotNull final Class<?> typeA, @NotNull final Class<?> typeB) {
        if (typeA == null || typeB == null) {
            return false;
        }
        if (typeA.isPrimitive() && typeB.isPrimitive()) {
            return typeA.getName().equals(typeB.getName());
        }
        if (typeA.isPrimitive()) {
            return typeA.getName().equals("byte") && typeB.equals(Byte.class) ||
                    typeA.getName().equals("short") && typeB.equals(Short.class) ||
                    typeA.getName().equals("int") && typeB.equals(Integer.class) ||
                    typeA.getName().equals("long") && typeB.equals(Long.class) ||
                    typeA.getName().equals("float") && typeB.equals(Float.class) ||
                    typeA.getName().equals("double") && typeB.equals(Double.class) ||
                    typeA.getName().equals("boolean") && typeB.equals(Boolean.class) ||
                    typeA.getName().equals("char") && typeB.equals(Character.class);
        }
        if (typeB.isPrimitive()) {
            return typeB.getName().equals("byte") && typeA.equals(Byte.class) ||
                    typeB.getName().equals("short") && typeA.equals(Short.class) ||
                    typeB.getName().equals("int") && typeA.equals(Integer.class) ||
                    typeB.getName().equals("long") && typeA.equals(Long.class) ||
                    typeB.getName().equals("float") && typeA.equals(Float.class) ||
                    typeB.getName().equals("double") && typeA.equals(Double.class) ||
                    typeB.getName().equals("boolean") && typeA.equals(Boolean.class) ||
                    typeB.getName().equals("char") && typeA.equals(Character.class);
        }
        return typeB.isAssignableFrom(typeA);
    }

    /**
     * Retrieves the primitive equivalent matching to the specified type.
     *
     * @param forType
     *         The type for which the primitive equivalent one shall be located. Can be {@code null}.
     * @return If any exists then the primitive equivalent type or {@code null}.
     */
    @Nullable
    static Class<?> getPrimitiveEquivalent(@Nullable final Class<?> forType) {
        if (forType != null) {
            if (forType.isPrimitive()) {
                if (forType.getName().equals("byte")) {
                    return Byte.class;
                }
                if (forType.getName().equals("short")) {
                    return Short.class;
                }
                if (forType.getName().equals("int")) {
                    return Integer.class;
                }
                if (forType.getName().equals("long")) {
                    return Long.class;
                }
                if (forType.getName().equals("float")) {
                    return Float.class;
                }
                if (forType.getName().equals("double")) {
                    return Double.class;
                }
                if (forType.getName().equals("boolean")) {
                    return Boolean.class;
                }
                if (forType.getName().equals("char")) {
                    return Character.class;
                }
            } else {
                if (forType.equals(Byte.class)) {
                    return byte.class;
                }
                if (forType.equals(Short.class)) {
                    return short.class;
                }
                if (forType.equals(Integer.class)) {
                    return int.class;
                }
                if (forType.equals(Long.class)) {
                    return long.class;
                }
                if (forType.equals(Float.class)) {
                    return float.class;
                }
                if (forType.equals(Double.class)) {
                    return double.class;
                }
                if (forType.equals(Boolean.class)) {
                    return boolean.class;
                }
                if (forType.equals(Character.class)) {
                    return char.class;
                }
            }
        }
        return null;
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
