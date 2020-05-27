package eu.redbean.uibuilder.components.chart.utils;

import java.util.Set;

public final class TypeUtils {

    private static final Set<Class<?>> NUMERIC_CLASSES = Set.of(
            byte.class, short.class, int.class, long.class, float.class, double.class, Number.class
    );

    public static boolean isNumeric(Class<?> type) {
        return NUMERIC_CLASSES.stream()
                .anyMatch(numClass -> numClass.isAssignableFrom(type));
    }
}
