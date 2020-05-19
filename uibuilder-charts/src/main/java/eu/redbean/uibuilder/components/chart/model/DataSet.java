package eu.redbean.uibuilder.components.chart.model;

import elemental.json.JsonObject;
import lombok.Data;

@Data
public class DataSet {

    private final String bindingPath;

    public DataSet(JsonObject jsonObject) {
        bindingPath = jsonObject.get("value").asString();
    }
}
