package eu.redbean.uibuilder.components.chart;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import eu.redbean.uibuilder.components.chart.events.PropertiesReadyEvent;
import eu.redbean.uibuilder.components.chart.model.ChartDataConverter;
import eu.redbean.uibuilder.components.chart.model.DataSet;
import eu.redbean.uibuilder.components.chart.model.Options;
import io.devbench.uibuilder.api.listeners.BackendAttachListener;
import io.devbench.uibuilder.data.api.datasource.DataSourceManager;
import io.devbench.uibuilder.data.api.datasource.DataSourceProvider;
import io.devbench.uibuilder.data.api.datasource.interfaces.DataSourceRefreshNotifiable;
import io.devbench.uibuilder.data.common.datasource.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@Tag(Chart.TAG_NAME)
@NpmPackage(value = "chart.js", version = "2.9.3")
@JsModule("./uibuilder-chart/src/uibuilder-chart.js")
public class Chart extends Component
        implements BackendAttachListener,
        DataSourceRefreshNotifiable,
        DataSourceChangeNotifiable<CommonDataSource<?, ?, ?, ?>> {

    public static final String TAG_NAME = "uibuilder-chart";

    private String dataSourceId;
    private String defaultQueryName;

    private List<DataSet> dataSets;
    private Options options;

    private ChartDataConverter dataConverter;


    @Override
    public void onAttached() {
        this.addListener(PropertiesReadyEvent.class, this::onPropertiesReady);
    }

    private void onPropertiesReady(PropertiesReadyEvent event) {
        this.dataSourceId = event.getDataSourceId();
        this.defaultQueryName = event.getDefaultQueryName();
        this.dataSets = event.getDataSets();
        this.options = event.getOptions();
        this.dataConverter = new ChartDataConverter(event.getLabelBindingPath(), dataSets);
        this.registerAsDataSourceReferenceHolder();
        reset();
    }

    private void registerAsDataSourceReferenceHolder() {
        CommonDataSourceContext<?> dataSourceContext = CommonDataSourceContext.getInstance();
        dataSourceContext.findDataSource(
                dataSourceId,
                new CommonDataSourceSelector(defaultQueryName, this)
        );
    }

    public void reset() {
        this.getElement().callJsFunction("_resetChart", dataConverter.convertToChartData(fetchItems()));
    }

    private List<?> fetchItems() {
        var dataSource = (CommonDataSource<?, ?, ?, ?>) DataSourceManager.getInstance().getDataSource(dataSourceId, new CommonDataSourceSelector(defaultQueryName, this));
        return dataSource.fetchData((PagingFetchRequest) null);
    }

    @Override
    public void refresh() {
        this.getElement().callJsFunction("_update", dataConverter.convertToChartData(fetchItems()));
    }

    @Override
    public void dataSourceChanged(@NotNull String dataSourceId,
                                  @NotNull CommonDataSourceSelector commonDataSourceSelector,
                                  @Nullable CommonDataSource<?, ?, ?, ?> commonDataSource) {
        reset(); //TODO check if really needed
    }
}
