package eu.redbean.uibuilder.components.charts.data;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.function.Function;

public abstract class DataFrameBuilderElement<ELEMENT, XAXIS_VALUE> {

    @Getter(AccessLevel.PACKAGE)
    private DataFrameBuilder<ELEMENT> dataFrameBuilder;

    protected DataFrameBuilderElement(DataFrameBuilder<ELEMENT> dataFrameBuilder) {
        this.dataFrameBuilder = dataFrameBuilder;
    }

    public DataFrameBuilder<ELEMENT> compile() {
        dataFrameBuilder.compiled = true;
        return dataFrameBuilder;
    }

    public DataColumnBuilder<ELEMENT, XAXIS_VALUE> column(String name,
                                                          AxisValueDataConverter<ELEMENT, XAXIS_VALUE> converter) {
        var columnBuilder = new DataColumnBuilder<>(name, converter, dataFrameBuilder);
        dataFrameBuilder.getColumnBuilders().add(columnBuilder);
        return columnBuilder;
    }

    public DataColumnBuilder<ELEMENT, XAXIS_VALUE> column(String name, Function<ELEMENT, Double> converter) {
        DataColumnBuilder<ELEMENT, XAXIS_VALUE> columnBuilder = new DataColumnBuilder<>(name, converter,
                dataFrameBuilder);
        dataFrameBuilder.getColumnBuilders().add(columnBuilder);
        return columnBuilder;
    }

}
