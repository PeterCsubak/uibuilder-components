package eu.redbean.uibuilder.components.chart.model.options;

import elemental.json.JsonObject;
import eu.redbean.uibuilder.components.chart.datatranslation.DefaultTranslationFunctions;
import io.devbench.uibuilder.core.controllerbean.uiproperty.PropertyConverter;
import io.devbench.uibuilder.core.controllerbean.uiproperty.PropertyConverters;
import io.devbench.uibuilder.core.controllerbean.uiproperty.converters.FunctionPropertyConverter;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

import static eu.redbean.uibuilder.components.chart.datatranslation.DefaultTranslationFunctions.getDefaults;

public class TranslateData {

    private static final PropertyConverter<Function<?, ?>, String> FUNCTION_CONVERTER = new FunctionPropertyConverter();

    private Function<?, ?> map;
    private Function<?, ?> reduce;

    public TranslateData(JsonObject object) {
        this.map = findFunctionFor("map", object);
        this.reduce = findFunctionFor("reduce", object);
    }

    private Function<?, ?> findFunctionFor(String key, JsonObject object) {
        if (object.hasKey(key)) {
            if (getDefaults().containsKey(object.getString(key))) {
                return getDefaults().get(object.getString(key));
            } else {
                return FUNCTION_CONVERTER.convertFrom(object.getString(key));
            }
        } else {
            return Function.identity();
        }
    }

    @SuppressWarnings("unchecked")
    public <I> Function<I, ?> map() {
        return (Function<I, ?>) this.map;
    }

    @SuppressWarnings("unchecked")
    public Function<List<?>, List<?>> reduce() {
        return (Function<List<?>, List<?>>) this.reduce;
    }

}
