package eu.redbean.uibuilder.components.chart.model;

import elemental.json.JsonObject;
import eu.redbean.uibuilder.components.chart.model.options.TranslateData;
import lombok.Data;

@Data
public class Options {

    private TranslateData translateData;

    public Options(JsonObject jsonObject) {
        //TODO IMPLEMENT others if needed
        if (jsonObject != null && jsonObject.hasKey("translateData")) {
            this.translateData = new TranslateData(jsonObject.getObject("translateData"));
        }
    }
}
