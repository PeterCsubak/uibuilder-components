package eu.redbean.uibuilder.components.chart.parse;

import com.vaadin.flow.component.Component;
import eu.redbean.uibuilder.components.chart.Chart;
import io.devbench.uibuilder.api.parse.ParseInterceptor;
import org.jsoup.nodes.Element;

public class ChartInterceptor implements ParseInterceptor {

    @Override
    public boolean isInstantiator(Element element) {
        return true;
    }

    @Override
    public Component instantiateComponent() {
        return new Chart<>();
    }

    @Override
    public void intercept(Component component, Element element) {

    }

    @Override
    public boolean isApplicable(Element element) {
        return Chart.TAG_NAME.equals(element.tagName());
    }
}
