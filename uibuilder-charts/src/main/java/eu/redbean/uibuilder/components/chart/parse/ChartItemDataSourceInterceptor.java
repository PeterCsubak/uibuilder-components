package eu.redbean.uibuilder.components.chart.parse;

import eu.redbean.uibuilder.components.chart.Chart;
import eu.redbean.uibuilder.components.chart.utils.ChartUtils;
import io.devbench.uibuilder.data.collectionds.interceptors.ItemDataSourceBindingContext;
import io.devbench.uibuilder.data.collectionds.interceptors.ItemDataSourceTagBasedParseInterceptor;
import org.jsoup.nodes.Element;

public class ChartItemDataSourceInterceptor extends ItemDataSourceTagBasedParseInterceptor {

    @Override
    public boolean isApplicable(Element element) {
        return Chart.TAG_NAME.equals(element.tagName());
    }

    @Override
    public ItemDataSourceBindingContext createBindingContext(Element element) {
        var ctx = super.createBindingContext(element);
        ChartUtils.collectBindings(element, ctx);
        return ctx;
    }
}
