package eu.redbean.uibuilder.data.rx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ReplayTest {

    public static void main(String[] args) throws InterruptedException {
        PublishSubject<String> test = PublishSubject.create();
        test.onNext("egy");
        ReplaySubject<String> rs = ReplaySubject.createWithSize(100);
        test.subscribe(rs::onNext);
        test.onNext("ketto");
        new Thread(() -> {
            IntStream.range(0, 100).forEach(i -> {
                test.onNext("i: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start();
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        rs.window(100, TimeUnit.MILLISECONDS)
            .subscribe(s -> {
                final var newItems = s.toList();
                newItems.subscribe(it -> {
                    if (!it.isEmpty()) {
                        list.addAll(it);
                        System.out.println("List changed, new items: " + String.join(", ", it));
                    }
                });
            });
    }

}
