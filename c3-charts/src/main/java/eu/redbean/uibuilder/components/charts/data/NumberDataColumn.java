package eu.redbean.uibuilder.components.charts.data;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonValue;
import io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Collection;

public class NumberDataColumn extends DataColumn<Double> {

    @Setter(AccessLevel.PACKAGE)
    private boolean convertToString; //TODO figure out a better way to handle this

    public NumberDataColumn(String name) {
        super(name);
    }

    public NumberDataColumn(String name, Collection<Double> values) {
        super(name, values);
    }

    @Override
    public JsonArray toJson() {
        return JsonBuilder.jsonArray()
                .add(this.getName())
                .addAll(
                        this.getValues().stream()
                                .map(this::asJsonValue)
                                .collect(JsonBuilder.jsonArrayCollector())
                ).build();
    }

    private JsonValue asJsonValue(Double value) {
        if (value == null) {
            return Json.createNull();
        } else {
            if (convertToString) {
                return Json.create(Double.toString(value));
            }
            return Json.create(value);
        }
    }

}
