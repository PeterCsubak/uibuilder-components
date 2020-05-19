package eu.redbean.uibuilder.components.chart.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import eu.redbean.uibuilder.components.chart.Chart;
import eu.redbean.uibuilder.components.chart.model.DataSet;
import eu.redbean.uibuilder.components.chart.model.Options;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@DomEvent("chart-properties-ready")
public class PropertiesReadyEvent extends ComponentEvent<Chart> {

    private final String dataSourceId;
    private final String dataSourceName;
    private final String defaultQueryName;
    private final String labelBindingPath;
    private final List<DataSet> dataSets;
    private final Options options;

    public PropertiesReadyEvent(Chart source, boolean fromClient,
                                @EventData("event.detail.dataSource.id") String dataSourceId,
                                @EventData("event.detail.dataSource.name") String dataSourceName,
                                @EventData("event.detail.dataSource.defaultQuery") String defaultQueryName,
                                @EventData("event.detail.labels.value") String labelBindingPath,
                                @EventData("event.detail.dataSets") JsonArray dataSets,
                                @EventData("event.detail.options") JsonObject options) {
        super(source, fromClient);
        this.dataSourceId = dataSourceId;
        this.dataSourceName = dataSourceName;
        this.defaultQueryName = defaultQueryName;
        this.labelBindingPath = labelBindingPath;
        this.dataSets = IntStream.range(0, dataSets.length())
                .mapToObj(dataSets::get)
                .filter(JsonObject.class::isInstance)
                .map(JsonObject.class::cast)
                .map(DataSet::new)
                .collect(Collectors.toList());
        this.options = new Options(options);
    }
}
