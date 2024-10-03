package org.uhanov;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.uhanov.config.AppConfig;

@ComponentScan("org.uhanov")
public class Application {
    static final Object lock = new Object();

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(
//                "org.uhanov"
        );
        context.getEnvironment()
                .getSystemProperties()
                .put(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "default");
        context.register(AppConfig.class);
        context.refresh();

        System.out.println("ACTIVE PROFILE=" +
                context.getBean(Environment.class)
                        .getActiveProfiles()[0]
        );
        context.close();

    }

}
