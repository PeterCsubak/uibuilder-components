package eu.redbean.uibuilder.components.chart.datatranslation;

import io.devbench.uibuilder.core.utils.reflection.ClassMetadata;
import io.devbench.uibuilder.core.utils.reflection.PropertyMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static eu.redbean.uibuilder.components.chart.utils.TypeUtils.isNumeric;

public final class DefaultTranslationFunctions {

    private static final Map<String, Function<?, ?>> defaults;

    private static <I> Set<TransposedNumeric> transposeWithConverter(
            I item,
            Function<PropertyMetadata<I>, TransposedNumeric> converter
    ) {
        var properties = ClassMetadata.ofValue(item).getProperties();
        return properties.stream()
                .map(converter)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static <I> Set<TransposedNumeric> transpose(I item) {
        return transposeWithConverter(item, DefaultTranslationFunctions::convertByName);
    }

    public static <I> Set<TransposedNumeric> transposeAnnotated(I item) {
        return transposeWithConverter(item, DefaultTranslationFunctions::convertByAnnotation);
    }

    private static <I> TransposedNumeric convertWithKey(PropertyMetadata<I> property, String key) {
        if (isNumeric(property.getType())) {
            var value = property.getValue();
            if (value != null) {
                return new TransposedNumeric(key, ((Number) value));
            }
        }
        return null;
    }

    private static <I> TransposedNumeric convertByName(PropertyMetadata<I> property) {
        return convertWithKey(property, property.getName());
    }

    private static <I> TransposedNumeric convertByAnnotation(PropertyMetadata<I> property) {
        if (property.isAnnotationPresent(DataPoint.class)) {
            return convertWithKey(property, property.getAnnotation(DataPoint.class).label());
        }

        return null;
    }

    public static List<TransposedNumeric> sumTransposed(Object transposedItems) {
        if (transposedItems instanceof List) {
            return ((List<?>)transposedItems).stream()
                    .filter(Collection.class::isInstance)
                    .map(obj -> (Collection<?>) obj)
                    .flatMap(Collection::stream)
                    .filter(TransposedNumeric.class::isInstance)
                    .map(TransposedNumeric.class::cast)
                    .collect(Collectors.groupingBy(
                            TransposedNumeric::getKey,
                            Collectors.summingDouble(it -> it.getValue().doubleValue())
                    )).entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> new TransposedNumeric(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    static {
        defaults = Map.of(
                "transpose", DefaultTranslationFunctions::transpose,
                "transposeAnnotated", DefaultTranslationFunctions::transposeAnnotated,
                "sumTransposed", DefaultTranslationFunctions::sumTransposed
        );
    }

    public static Map<String, Function<?, ?>> getDefaults() {
        return defaults;
    }

    @Data
    @AllArgsConstructor
    public static class TransposedNumeric {
        private String key;
        private Number value;
    }

}
