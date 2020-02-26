package eu.redbean.uibuilder.components.charts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import eu.redbean.uibuilder.components.charts.data.DataFrameBuilder;

import java.util.Collection;
import java.util.Objects;

import static eu.redbean.uibuilder.components.charts.LineChart.*;

@Tag(TAG_NAME)
@HtmlImport("frontend://bower_components/c3-chart/src/line-chart.html")
public class LineChart<T> extends Component {

    public static final String TAG_NAME = "line-chart";

    private DataFrameBuilder<T> dataFrameBuilder;


    public void setDataFrameBuilder(DataFrameBuilder<T> dataFrameBuilder) {
        if (this.dataFrameBuilder != null) {
            throw new IllegalStateException("Data-frame builder already set");
        }
        if (dataFrameBuilder == null || !dataFrameBuilder.isCompiled()) {
            throw new IllegalArgumentException("Data-frame builder has to be compiled to use in chart");
        }
        this.dataFrameBuilder = dataFrameBuilder;
    }

    public void setItems(Collection<T> items) {
        Objects.requireNonNull(dataFrameBuilder);
        getElement().callJsFunction(
            "__destroy_and_rebuild",
            dataFrameBuilder.convertToInitialDataJson(items)
        );
    }

    public void pushAdditionalItems(Collection<T> additionalItems, Integer pushLength, Integer transitionDuration) {
        Objects.requireNonNull(dataFrameBuilder);
        getElement().callJsFunction(
            "flow",
            dataFrameBuilder.convertToDataFrame(additionalItems, pushLength, transitionDuration)
        );
        if (dataFrameBuilder.providesXGridLines()) {
            var gridLinesJson = dataFrameBuilder.convertToXGridLines(additionalItems);
            if (gridLinesJson.length() > 0) {
                getElement().callJsFunction("addXGrids", gridLinesJson);
            }
        }
    }

    public void pushAdditionalItems(Collection<T> additionalItems, int pushLength) {
        this.pushAdditionalItems(additionalItems, pushLength, null);
    }

    public void pushAdditionalItems(Collection<T> additionalItems) {
        this.pushAdditionalItems(additionalItems, null, null);
    }

    //TODO add configs

}
