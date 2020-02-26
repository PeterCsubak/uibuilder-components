package eu.redbean.uibuilder.components.charts.data;

import elemental.json.JsonArray;

import java.time.LocalDate;

public class LocalDateDataColumn extends DataColumn<LocalDate> {

    public LocalDateDataColumn(String name) {
        super(name);
    }

    @Override
    public JsonArray toJson() {
        return null; //TODO impl for axis data
    }

}
