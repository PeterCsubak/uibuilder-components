package eu.redbean.uibuilder.components.charts.data;

import java.util.Collection;

@FunctionalInterface
public interface AxisValueDataConverter<ELEMENT, AXIS_VALUE> {

    Double convert(Collection<ELEMENT> elements, AXIS_VALUE atAxisPoint);

}
