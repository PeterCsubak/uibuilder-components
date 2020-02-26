package eu.redbean.uibuilder.components.demoapp.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestData {

    private int index;
    private double elephants;
    private Integer horses;
    private float chickens;

}
