package eu.redbean.uibuilder.components.chart.model;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonValue;
import io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder;
import io.devbench.uibuilder.core.controllerbean.StringPropertyConverter;
import io.devbench.uibuilder.core.controllerbean.uiproperty.Converter;
import io.devbench.uibuilder.core.controllerbean.uiproperty.JsonPropertyConverter;
import io.devbench.uibuilder.core.controllerbean.uiproperty.PropertyConverter;
import io.devbench.uibuilder.core.controllerbean.uiproperty.PropertyConverters;
import io.devbench.uibuilder.core.utils.reflection.ClassMetadata;
import io.devbench.uibuilder.core.utils.reflection.PropertyMetadata;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ChartDataConverter {

    private static final Set<Class<?>> NUMERIC_CLASSES = Set.of(
            byte.class, short.class, int.class, long.class, float.class, double.class, Number.class
    );

    private final String labelsBindingPath;
    private final List<DataSet> dataSets;

    public JsonArray convertToChartData(List<?> items) {
        if (items.isEmpty()) {
            return Json.createArray();
        }

        Map<String, JsonBuilder.JsonArrayBuilder> bindingsToArrays = new HashMap<>();
        bindingsToArrays.put(labelsBindingPath, JsonBuilder.jsonArray());
        bindingsToArrays.putAll(
                dataSets.stream()
                        .map(DataSet::getBindingPath)
                        .collect(Collectors.toMap(Function.identity(), key -> JsonBuilder.jsonArray()))
        );

        items.stream()
                .map(ClassMetadata::ofValue)
                .forEach(meta -> {
                    bindingsToArrays.forEach((binding, array) ->
                            array.add(convertToJsonValue(meta.property(binding).orElse(null))));
                });

        return JsonBuilder.jsonArray()
                .add(bindingsToArrays.get(labelsBindingPath).build())
                .addAll(dataSets.stream()
                        .map(DataSet::getBindingPath)
                        .map(bindingsToArrays::get)
                        .map(JsonBuilder.JsonArrayBuilder::build)
                        .collect(Collectors.toList()))
                .build();
    }

    private JsonValue convertToJsonValue(@Nullable PropertyMetadata<?> property) {
        if (property != null && PropertyConverters.isConvertibleProperty(property)) {
            PropertyConverter<?, ?> propertyConverter = PropertyConverters.getConverterFor(property);
            if (propertyConverter instanceof JsonPropertyConverter) {
                return ((JsonPropertyConverter<?>) propertyConverter).convertTo(property.getValue());
            } else {
                if (!property.isAnnotationPresent(Converter.class) && isNumeric(property.getType())) {
                    return Json.create(((Number) property.getValue()).doubleValue());
                }

                if (propertyConverter instanceof StringPropertyConverter) {
                    return Json.create(((StringPropertyConverter<?>) propertyConverter).convertTo(property.getValue()));
                }
            }
        }

        return Json.createNull();
    }

    private boolean isNumeric(Class<?> type) {
        return NUMERIC_CLASSES.stream()
                .anyMatch(numClass -> numClass.isAssignableFrom(type));
    }

}
