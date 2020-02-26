package eu.redbean.uibuilder.components.charts.data;

import elemental.json.JsonArray;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public abstract class DataColumn<T> {

    private String name;
    private Collection<T> values;

    public DataColumn(String name, Collection<T> values) {
        this.name = name;
        this.values = values;
    }

    public DataColumn(String name) {
        this(name, new ArrayList<>());
    }

    public abstract JsonArray toJson();

}
