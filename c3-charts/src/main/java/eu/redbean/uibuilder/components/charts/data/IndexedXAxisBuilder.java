package eu.redbean.uibuilder.components.charts.data;

import java.util.Collection;

public class IndexedXAxisBuilder<ELEMENT> extends XAxisBuilder<ELEMENT, Double> {

    IndexedXAxisBuilder(String name, AxisConverter<ELEMENT, Double> converter, DataFrameBuilder<ELEMENT> dataFrameBuilder) {
        super(name, converter, dataFrameBuilder);
    }

    @Override
    protected String getType() {
        return "category";
    }

    @Override
    protected DataColumn<Double> columnFromElements(Collection<ELEMENT> elements) {
        return new NumberDataColumn(this.getName(), this.getConverter().convert(elements));
    }

}
