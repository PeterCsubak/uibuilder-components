package eu.redbean.uibuilder.components.charts.data;

import elemental.json.JsonObject;
import eu.redbean.uibuilder.components.charts.utils.json.JsonUtil;
import io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Builder
@EqualsAndHashCode
public class NumericGridLine { //TODO make it more abstract to use with timeseries too

    public enum Position {
        START("start"), MIDDLE("middle"), END("end");

        private String value;

        Position(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private double value;
    private String text;
    private Position position;
    private String cssClass;

    public JsonObject toJson() {
        return JsonBuilder.jsonObject()
                .put("value", Double.toString(value))
                .put("text", Optional.ofNullable(text).orElse(Double.toString(value)))
                .put("position", Optional.ofNullable(position).orElse(Position.END).toString())
                .putIf(cssClass != null, "class", JsonUtil.safeCreate(cssClass))
                .build();
    }

}
