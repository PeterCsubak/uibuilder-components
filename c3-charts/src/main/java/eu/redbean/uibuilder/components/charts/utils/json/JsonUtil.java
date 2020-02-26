package eu.redbean.uibuilder.components.charts.utils.json;

import elemental.json.Json;
import elemental.json.JsonNumber;
import elemental.json.JsonString;

public class JsonUtil {

    public static JsonNumber safeCreate(Number value) {
        if (value != null) {
            return Json.create(value.doubleValue());
        }
        return null;
    }

    public static JsonString safeCreate(String value) {
        if (value != null) {
            return Json.create(value);
        }
        return null;
    }

}
