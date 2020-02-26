package eu.redbean.uibuilder.components.charts.data;

import elemental.json.JsonArray;

import java.time.LocalDateTime;

public class LocalDateTimeDataColumn extends DataColumn<LocalDateTime> {

    public LocalDateTimeDataColumn(String name) {
        super(name);
    }

    @Override
    public JsonArray toJson() {
        return null; // TODO impl for axis data
    }
}
