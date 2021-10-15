package eu.redbean.uibuilder.components.chart;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import elemental.json.Json;
import eu.redbean.uibuilder.components.chart.events.PropertiesReadyEvent;
import eu.redbean.uibuilder.components.chart.model.ChartDataConverter;
import eu.redbean.uibuilder.components.chart.model.DataSet;
import eu.redbean.uibuilder.components.chart.model.Options;
import io.devbench.uibuilder.api.listeners.BackendAttachListener;
import io.devbench.uibuilder.data.api.datasource.DataSourceManager;
import io.devbench.uibuilder.data.api.datasource.interfaces.DataSourceRefreshNotifiable;
import io.devbench.uibuilder.data.api.filter.FilterExpression;
import io.devbench.uibuilder.data.collectionds.ItemDataSourceCapable;
import io.devbench.uibuilder.data.common.datasource.*;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Tag(Chart.TAG_NAME)
@NpmPackage(value = "chart.js", version = "2.9.3")
@NpmPackage(value = "chartjs-plugin-colorschemes", version = "0.4.0")
@NpmPackage(value = "chartjs-plugin-zoom", version = "0.7.7")
@JsModule("./uibuilder-chart/src/uibuilder-chart.js")
public class Chart<T> extends Component
        implements BackendAttachListener,
        DataSourceRefreshNotifiable,
        ItemDataSourceCapable<T>,
        SupportsDataSourceReuse {

    public static final String TAG_NAME = "uibuilder-chart";

    private final String dataSourceContextId;

    @Setter
    private String dataSourceId;
    private String defaultQueryName;

    private List<DataSet> dataSets;
    private Options options;

    private ChartDataConverter dataConverter;


    public Chart() {
        this.dataSourceContextId = UUID.randomUUID().toString();
    }

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
        List<?> items = dataSource.fetchData((PagingFetchRequest) null);
        return translateDataIfRequired(items);
    }

    private List<?> translateDataIfRequired(List<?> items) {
        var translateData = options.getTranslateData();
        if (translateData != null) {
            return translateData.reduce().apply(
                    items.stream()
                            .map(translateData.map())
                            .collect(Collectors.toList())
            ); // TODO rewrite to reduce(map(list)) form, because that way map can return a Stream, and reduce can operate on that same stream
        } else {
            return items;
        }
    }

    @Override
    public void refresh() {
        this.getElement().callJsFunction("_update", dataConverter.convertToChartData(fetchItems()));
    }

    @Override
    public void connectItemDataSource(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setDataSource(CommonDataSource<T, ?, ? extends FilterExpression<?>, ?> collectionDataSource) {
        CommonDataSourceContext<CommonDataSource<T, ?, ?, ?>> dataSourceContext =
                (CommonDataSourceContext<CommonDataSource<T, ?, ?, ?>>) CommonDataSourceContext.getInstance();
        dataSourceContext.replaceDataSource(this.dataSourceId, new CommonDataSourceSelector(null, this), () -> collectionDataSource);
    }

    @Override
    public String getContextId() {
        return dataSourceContextId;
    }

    public void pushNewItems(List<T> items, boolean removeOld) {
        this.getElement().callJsFunction("_pushNewData",
                dataConverter.convertToChartData(translateDataIfRequired(items)),
                Json.create(removeOld));
    }

}
