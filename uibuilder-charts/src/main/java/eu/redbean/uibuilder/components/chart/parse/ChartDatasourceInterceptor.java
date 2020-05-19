package eu.redbean.uibuilder.components.chart.parse;

import eu.redbean.uibuilder.components.chart.Chart;
import eu.redbean.uibuilder.components.chart.utils.ChartUtils;
import io.devbench.uibuilder.components.util.datasource.DataSourceBindingContext;
import io.devbench.uibuilder.components.util.datasource.DataSourceTagBasedParseInterceptor;
import org.jsoup.nodes.Element;

public class ChartDatasourceInterceptor extends DataSourceTagBasedParseInterceptor {

    //TODO remove these probably not needed

    @Override
    public boolean isApplicable(Element element) {
        return Chart.TAG_NAME.equals(element.tagName());
    }

    @Override
    public DataSourceBindingContext createBindingContext(Element element) {
        var ctx = super.createBindingContext(element);
        ChartUtils.collectBindings(element, ctx);
        return ctx;
    }

}
