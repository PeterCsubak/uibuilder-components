package eu.redbean.uibuilder.components.demoapp.controllerbeans;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.redbean.uibuilder.components.chart.Chart;
import eu.redbean.uibuilder.components.demoapp.data.TestData;
import io.devbench.uibuilder.annotations.ControllerBean;
import io.devbench.uibuilder.api.controllerbean.UIComponent;
import io.devbench.uibuilder.api.controllerbean.uieventhandler.UIEventHandler;

import javax.inject.Provider;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UIScope
@ControllerBean("chartDemo")
public class ChartDemoBean {

    @UIComponent("firstChart")
    private Provider<Chart<TestData>> chartProvider;

    private List<TestData> items = new ArrayList<>();
    private AtomicInteger indexMax = new AtomicInteger(0);

    private Thread pushingThread;
    private AtomicBoolean stopped = new AtomicBoolean(false);

    private static TestData generateRandomData(int index) {
        var random = new Random();
        return TestData.builder()
                .index(index)
                .elephants(random.nextDouble())
                //.horses(random.nextBoolean() ? random.nextInt(10) : null)
                .horses(random.nextInt(10))
                .chickens(random.nextFloat())
                .build();
    }

    public ChartDemoBean() {
        IntStream.range(0, 20)
                .boxed()
                .map(ChartDemoBean::generateRandomData)
                .forEach(this.items::add);
        indexMax.set(20);
    }

    @UIEventHandler("init")
    public void onInit() {

    }

    @UIEventHandler("addMoreData")
    public void onAddMoreData() {
        var newItems = IntStream.range(indexMax.get(), indexMax.addAndGet(5))
                .boxed()
                .map(ChartDemoBean::generateRandomData)
                .collect(Collectors.toList());

        chartProvider.get().pushNewItems(newItems, true);
    }

    @UIEventHandler("toggleContinuous")
    public void onToggleContinuous(@UIComponent("addMoreBtn") Button addBtn,
                                   @UIComponent("continuous") Button conBtn) {
        if (conBtn.getThemeName() == null || "primary success".equals(conBtn.getThemeName())) {
            this.startDataPushingThread(chartProvider.get(), UI.getCurrent());
            addBtn.setEnabled(false);
            conBtn.setThemeName("primary error");
            conBtn.setText("Stop");
        } else {
            this.stopDataPushingThread();
            addBtn.setEnabled(true);
            conBtn.setThemeName("primary success");
            conBtn.setText("Start continuously adding");
        }
    }

    private void startDataPushingThread(Chart<TestData> chart, UI ui) {
        stopped.set(false);
        pushingThread = new Thread(() -> {
            while (!stopped.get()) {
                int increment = 1;//new Random().nextInt(2) + 1;
                var newItems = IntStream.range(indexMax.get(), indexMax.addAndGet(increment))
                    .boxed()
                    .map(ChartDemoBean::generateRandomData)
                    .collect(Collectors.toList());

                ui.access(() -> chart.pushNewItems(newItems, true));
                try {
                    Thread.sleep(350);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pushingThread.start();
    }

    private void stopDataPushingThread() {
        stopped.set(true);
        pushingThread = null;
    }
}
