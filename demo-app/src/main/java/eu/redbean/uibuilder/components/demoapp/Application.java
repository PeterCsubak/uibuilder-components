package eu.redbean.uibuilder.components.demoapp;

import io.devbench.uibuilder.annotations.EnableUIBuilder;
import io.devbench.uibuilder.annotations.StaticContent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableUIBuilder("eu.redbean.uibuilder.components")
@SpringBootApplication(scanBasePackages = "eu.redbean.uibuilder.components")
@StaticContent(lookupPaths = "static/", url = "static/")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
