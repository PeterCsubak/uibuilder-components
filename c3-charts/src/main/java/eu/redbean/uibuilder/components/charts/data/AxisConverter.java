package eu.redbean.uibuilder.components.charts.data;

import java.util.Collection;

public interface AxisConverter<ELEMENT, VALUE_TYPE> {

    Collection<VALUE_TYPE> convert(Collection<ELEMENT> elements);

}
