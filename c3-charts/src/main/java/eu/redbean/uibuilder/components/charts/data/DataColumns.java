package eu.redbean.uibuilder.components.charts.data;

import elemental.json.JsonArray;
import io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DataColumns {

    @Getter
    private List<DataColumn<?>> columns;

    public DataColumns() {
        this.columns = new ArrayList<>();
    }

    public JsonArray toJson() {
        return columns.stream()
                .map(DataColumn::toJson)
                .collect(JsonBuilder.jsonArrayCollector());
    }

}
