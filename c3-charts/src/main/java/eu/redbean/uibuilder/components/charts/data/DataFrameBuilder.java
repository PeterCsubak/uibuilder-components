package eu.redbean.uibuilder.components.charts.data;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import eu.redbean.uibuilder.components.charts.utils.json.JsonUtil;
import io.devbench.uibuilder.api.utils.elemental.json.JsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter(AccessLevel.PACKAGE)
public class DataFrameBuilder<ELEMENT> {

    @Getter(AccessLevel.PUBLIC)
    boolean compiled;

    private XAxisBuilder<ELEMENT, ?> xAxisBuilder;
    private List<DataColumnBuilder<ELEMENT, ?>> columnBuilders;
    private boolean labels;
    private Function<ELEMENT, NumericGridLine> xGridLinesProvider;


    public DataFrameBuilder() {
        this.compiled = false;
        this.columnBuilders = new ArrayList<>();
        this.xGridLinesProvider = null;
    }

    public DataFrameBuilder<ELEMENT> xGridLines(Function<ELEMENT, NumericGridLine> xGridLinesProvider) {
        this.xGridLinesProvider = xGridLinesProvider;
        return this;
    }

    public IndexedXAxisBuilder<ELEMENT> indexedXAxis(String name, AxisConverter<ELEMENT, Double> converter) {
        var axisBuilder = new IndexedXAxisBuilder<>(name, converter, this);
        this.xAxisBuilder = axisBuilder;
        return axisBuilder;
    }

    public JsonObject convertToInitialDataJson(Collection<ELEMENT> elements) {
        DataColumns columns = buildDataFrameColumns(elements);

        var dataNames = JsonBuilder.jsonObject();
        var dataColors = JsonBuilder.jsonObject();
        var xs = JsonBuilder.jsonObject();
        columnBuilders.forEach(it -> {
            dataNames.putIf(it.getLabel() != null, it.getName(), JsonUtil.safeCreate(it.getLabel()));
            dataColors.putIf(it.getColor() != null, it.getName(), JsonUtil.safeCreate(it.getColor()));
            xs.put(it.getName(), xAxisBuilder.getName());
        });

        JsonArray gridLines = null;
        if (xGridLinesProvider != null) {
            gridLines = createXGridLinesFor(elements);
        }

        return JsonBuilder.jsonObject()
            .put("data",
                JsonBuilder.jsonObject()
                    .put("x", xAxisBuilder.getName())
                    .put("columns", columns.toJson())
                    .put("labels", labels)
                    .put("names", dataNames.build())
                    .put("colors", dataColors.build())
                    .put("xs", xs.build())
                    .build()
            )
            .put("axis", xAxisBuilder.toAxisJson())
            .putIf(gridLines != null, "xGridLines", gridLines)
            .build();
    }

    private JsonArray createXGridLinesFor(Collection<ELEMENT> elements) {
        return elements.stream()
            .map(xGridLinesProvider)
            .filter(Objects::nonNull)
            .distinct()
            .map(NumericGridLine::toJson)
            .collect(JsonBuilder.jsonArrayCollector());
    }

    @NotNull
    private DataColumns buildDataFrameColumns(Collection<ELEMENT> elements) {
        if (!this.compiled) {
            throw new IllegalStateException("Unfinished data-frame builder");
        }

        DataColumn<?> xAxisColumn = xAxisBuilder.columnFromElements(elements);
        if (xAxisColumn instanceof NumberDataColumn) {
            ((NumberDataColumn) xAxisColumn).setConvertToString(true);
        }
        var columns = new DataColumns();
        columns.getColumns().add(xAxisColumn);
        columnBuilders.stream()
            .map(it -> it.columnFromElements(elements, xAxisColumn.getValues()))
            .forEach(columns.getColumns()::add);
        return columns;
    }

    public JsonObject convertToDataFrame(Collection<ELEMENT> additionalElements,
                                         Integer flowPushLength,
                                         Integer flowTransitionDuration) {
        var columns = buildDataFrameColumns(additionalElements);
        return JsonBuilder.jsonObject()
            .put("columns", columns.toJson())
            .putIf(flowPushLength != null, "length", JsonUtil.safeCreate(flowPushLength))
            .putIf(flowTransitionDuration != null, "duration", JsonUtil.safeCreate(flowTransitionDuration))
            .build();
    }

    public boolean providesXGridLines() {
        return this.xGridLinesProvider != null;
    }

    public JsonArray convertToXGridLines(Collection<ELEMENT> additionalItems) {
        if (xGridLinesProvider != null) {
            return createXGridLinesFor(additionalItems);
        } else {
            throw new IllegalStateException("xGridLinesProvider not set");
        }
    }




    /*
    dfb = new DataFrameBuilder();
    dfb.xAxis(es -> ...).name("x name");
    dfb.column((els, av) -> ...).name("d1").color("#fff");
    dfb.column((els, av) -> ...).name("d2").color("#000");
    dfb.labels(true);
    dfb.xGridLines((els) -> ...)
      //.regions(new RegionsBuilder()...) later

    chart.setDataFrameBuilder(dfb);
    chart.setItems(elements);

    chart.sendDataFrame(newElements, pushLength, duration)
     */

}
