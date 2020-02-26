package eu.redbean.uibuilder.components.charts.data;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collection;
import java.util.function.Function;

@Getter(AccessLevel.PACKAGE)
public class DataColumnBuilder<ELEMENT, XAXIS_VALUE> extends DataFrameBuilderElement<ELEMENT, XAXIS_VALUE> {

    private final String name;
    private final AxisValueDataConverter<ELEMENT, XAXIS_VALUE> axisValueConverter;
    private final Function<ELEMENT, Double> simpleValueConverter;
    private String label;
    private String color;

    //TODO add mappedTo(axis) for y and y2 mappings

    DataColumnBuilder(String name,
                      AxisValueDataConverter<ELEMENT, XAXIS_VALUE> converter,
                      DataFrameBuilder<ELEMENT> dataFrameBuilder) {
        super(dataFrameBuilder);
        this.name = name;
        this.axisValueConverter = converter;
        this.simpleValueConverter = null;
    }

    DataColumnBuilder(String name, Function<ELEMENT, Double> converter, DataFrameBuilder<ELEMENT> dataFrameBuilder) {
        super(dataFrameBuilder);
        this.name = name;
        this.axisValueConverter = null;
        this.simpleValueConverter = converter;
    }

    public DataColumnBuilder<ELEMENT, XAXIS_VALUE> label(String label) {
        this.label = label;
        return this;
    }

    public DataColumnBuilder<ELEMENT, XAXIS_VALUE> color(String color) {
        this.color = color;
        return this;
    }

    @SuppressWarnings("unchecked")
    NumberDataColumn columnFromElements(Collection<ELEMENT> elements, Collection<?> xaxisValues) {
        var column = new NumberDataColumn(this.name);

        if (this.axisValueConverter != null) {
            xaxisValues.stream()
                    .map(it -> this.axisValueConverter.convert(elements, (XAXIS_VALUE) it))
                    .forEach(column.getValues()::add);
        } else {
            assert this.simpleValueConverter != null;
            elements.stream()
                    .map(this.simpleValueConverter)
                    .forEach(column.getValues()::add);
        }

        return column;
    }

}
