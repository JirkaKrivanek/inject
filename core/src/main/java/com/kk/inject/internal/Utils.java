package com.kk.inject.internal;

import com.kk.inject.Nullable;

/**
 * Various static utilities.
 */
public final class Utils {

    /**
     * Checks whether item is in array.
     *
     * @param array
     *         The array. Can be {@code null}.
     * @param item
     *         The item. Can be {@code null}.
     * @return If item in array then {@code true}. If item not in array or no array then {@code false}.
     */
    public static boolean contains(@Nullable final Object[] array, @Nullable final Object item) {
        if (array != null) {
            for (int index = 0; index < array.length; index++) {
                final Object arrayItem = array[index];
                if (arrayItem == null && item == null) {
                    return true;
                }
                if (arrayItem != null && item != null && arrayItem.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
