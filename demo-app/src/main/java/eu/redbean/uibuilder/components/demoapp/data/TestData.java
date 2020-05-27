package eu.redbean.uibuilder.components.demoapp.data;

import eu.redbean.uibuilder.components.chart.datatranslation.DataPoint;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestData {

    private int index;

    @DataPoint(label = "Elephants")
    private double elephants;

    @DataPoint(label = "Horses")
    private Integer horses;

    @DataPoint(label = "Chickens")
    private float chickens;

}
