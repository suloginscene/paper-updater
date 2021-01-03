package me.scene.paper.updater;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class App {

    public static final String SPRING_CONFIGS = "spring.config.location="
            + "classpath:application.yml,"
            + "file:/paper/conf/updater.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class)
                .properties(SPRING_CONFIGS)
                .run(args);
    }

}
