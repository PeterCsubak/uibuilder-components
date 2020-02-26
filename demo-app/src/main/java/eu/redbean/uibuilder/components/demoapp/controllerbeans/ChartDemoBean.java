package eu.redbean.uibuilder.components.demoapp.controllerbeans;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.redbean.uibuilder.components.charts.LineChart;
import eu.redbean.uibuilder.components.charts.data.DataFrameBuilder;
import eu.redbean.uibuilder.components.demoapp.data.TestData;
import io.devbench.uibuilder.annotations.ControllerBean;
import io.devbench.uibuilder.api.controllerbean.UIComponent;
import io.devbench.uibuilder.api.controllerbean.uieventhandler.UIEventHandler;
import lombok.AllArgsConstructor;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UIScope
@ControllerBean("chartDemo")
public class ChartDemoBean {

    @UIComponent("chart")
    private Provider<LineChart<TestData>> chartProvider;

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

    @UIEventHandler("init")
    public void onInit() {
        var chart = chartProvider.get();
        IntStream.range(0, 50)
                .boxed()
                .map(ChartDemoBean::generateRandomData)
                .forEach(this.items::add);
        indexMax.set(50);

        chart.setDataFrameBuilder(
                new DataFrameBuilder<TestData>()
                        .indexedXAxis("idx", els -> els.stream()
                                .map(TestData::getIndex)
                                .map(Double::valueOf)
                                .collect(Collectors.toList())
                        )
                        .label("Data index")
                        .tickCulling(true)
                        .tickCullingMax(50)
                        .column("elephants", TestData::getElephants).label("Elephants").color("#000099")
                        .column("horses",
                                it -> Optional.ofNullable(it.getHorses()).map(Integer::doubleValue).orElse(null))
                        .label("Horses")
                        .column("chickens", it -> (double) it.getChickens())
                        .compile()
        );

        chart.setItems(items);
    }

    @UIEventHandler("addMoreData")
    public void onAddMoreData() {
        var chart = chartProvider.get();
        chart.pushAdditionalItems(
                IntStream.range(indexMax.get(), indexMax.addAndGet(50))
                        .boxed()
                        .map(ChartDemoBean::generateRandomData)
                        .collect(Collectors.toList()),
                0);
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

    private void startDataPushingThread(LineChart<TestData> chart, UI ui) {
        if (pushingThread == null) {
            pushingThread = new PushingThread(indexMax, chart, ui);
            pushingThread.start();
        }
    }

    private void stopDataPushingThread() {
        if (pushingThread != null && pushingThread.isAlive()) {
            try {
                pushingThread.interrupt();
                pushingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pushingThread = null;
        }
    }

    @AllArgsConstructor
    public static class PushingThread extends Thread {

        private AtomicInteger indexMax;
        private LineChart<TestData> chart;
        private UI ui;

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(2000);
                    ui.access(() -> {
                        chart.pushAdditionalItems(
                                IntStream.range(indexMax.get(), indexMax.addAndGet(10))
                                        .boxed()
                                        .map(ChartDemoBean::generateRandomData)
                                        .collect(Collectors.toList()));
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

}
