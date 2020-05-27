package eu.redbean.uibuilder.components.demoapp.controllerbeans;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.redbean.uibuilder.components.demoapp.data.TestData;
import io.devbench.uibuilder.annotations.ControllerBean;
import io.devbench.uibuilder.api.controllerbean.UIComponent;
import io.devbench.uibuilder.api.controllerbean.uieventhandler.UIEventHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UIScope
@ControllerBean("chartDemo")
public class ChartDemoBean {


    private List<TestData> items = new ArrayList<>();
    private AtomicInteger indexMax = new AtomicInteger(0);

    private Thread pushingThread;

    private static TestData generateRandomData(int index) {
        var random = new Random();
        return TestData.builder()
                .index(index)
                .elephants(random.nextDouble())
                .horses(random.nextBoolean() ? random.nextInt(10) : null)
                .chickens(random.nextFloat())
                .build();
    }

    public ChartDemoBean() {
        IntStream.range(0, 20)
                .boxed()
                .map(ChartDemoBean::generateRandomData)
                .forEach(this.items::add);
    }

    @UIEventHandler("init")
    public void onInit() {

    }

    @UIEventHandler("addMoreData")
    public void onAddMoreData() {
//        var chart = chartProvider.get();
//        chart.pushAdditionalItems(
//                IntStream.range(indexMax.get(), indexMax.addAndGet(50))
//                        .boxed()
//                        .map(ChartDemoBean::generateRandomData)
//                        .collect(Collectors.toList()),
//                0);
    }

    @UIEventHandler("toggleContinuous")
    public void onToggleContinuous(@UIComponent("addMoreBtn") Button addBtn,
                                   @UIComponent("continuous") Button conBtn) {
//        if (conBtn.getThemeName() == null || "primary success".equals(conBtn.getThemeName())) {
//            this.startDataPushingThread(chartProvider.get(), UI.getCurrent());
//            addBtn.setEnabled(false);
//            conBtn.setThemeName("primary error");
//            conBtn.setText("Stop");
//        } else {
//            this.stopDataPushingThread();
//            addBtn.setEnabled(true);
//            conBtn.setThemeName("primary success");
//            conBtn.setText("Start continuously adding");
//        }
    }


}
