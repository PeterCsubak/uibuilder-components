package eu.redbean.uibuilder.components.chart.utils;

import io.devbench.uibuilder.components.util.datasource.BaseDataSourceBindingContext;
import io.devbench.uibuilder.components.util.datasource.DataSourceBindingContext;
import org.jsoup.nodes.Element;

public class ChartUtils {

    private static final String VALUE = "value";
    private static final String ITEM_PREFIX = "item.";

    public static void collectBindings(Element element, BaseDataSourceBindingContext ctx) {
        element.children().stream()
                .filter(el -> el.hasAttr(VALUE))
                .map(el -> el.attr(VALUE))
                .map(s -> ITEM_PREFIX + s)
                .forEach(ctx.getBindings()::add);
    }
}
