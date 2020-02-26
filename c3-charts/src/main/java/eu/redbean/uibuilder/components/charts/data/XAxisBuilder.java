package eu.redbean.uibuilder.components.charts.data;

import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;

import static io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder.jsonObject;

@Getter(AccessLevel.PACKAGE)
public abstract class XAxisBuilder<ELEMENT, VALUE_TYPE> extends DataFrameBuilderElement<ELEMENT, VALUE_TYPE> {

    private String name;
    private String label;
    private AxisConverter<ELEMENT, VALUE_TYPE> converter;
    // private String valueFormat; //TODO check type of converter and set automatically
    private boolean tickCulling;
    private int tickCullingMax;
    private int tickRotate;
    //TODO add label position option

    public XAxisBuilder(String name, AxisConverter<ELEMENT, VALUE_TYPE> converter, DataFrameBuilder<ELEMENT> dataFrameBuilder) { //TODO remove name
        super(dataFrameBuilder);
        this.name = name;
        this.converter = converter;
        this.label = "x";
        this.tickCulling = false;
        this.tickCullingMax = 10;
        this.tickRotate = 0;
    }

    public XAxisBuilder<ELEMENT, VALUE_TYPE> label(String label) {
        if (label == null) {
            label = "x";
        }
        this.label = label;
        return this;
    }

    public XAxisBuilder<ELEMENT, VALUE_TYPE> tickCulling(boolean tickCulling) {
        this.tickCulling = tickCulling;
        return this;
    }

    public XAxisBuilder<ELEMENT, VALUE_TYPE> tickCullingMax(int tickCullingMax) {
        this.tickCullingMax = tickCullingMax;
        return this;
    }

    public XAxisBuilder<ELEMENT, VALUE_TYPE> tickRotate(int tickRotate) {
        this.tickRotate = tickRotate;
        return this;
    }

    protected abstract String getType();

    JsonObject toAxisJson() {
        JsonObject tick = jsonObject()
                .put("culling", tickCulling
                        ? jsonObject().put("max", tickCullingMax).build()
                        : Json.create(false))
                .put("rotate", tickRotate)
                .put("multiline", false)
                .build();

        JsonObject xAxis = jsonObject()
                .put("label", label)
                .put("type", getType())
                .put("tick", tick)
                .build();

        return jsonObject()
                .put("x", xAxis)
                .build();
    }

    protected  abstract DataColumn<VALUE_TYPE> columnFromElements(Collection<ELEMENT> elements);

}
