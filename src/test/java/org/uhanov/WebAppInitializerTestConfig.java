package org.uhanov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.uhanov.config.AppConfig;
import org.uhanov.config.DispatcherConfig;
import org.uhanov.config.DispatcherConfigTest;
import org.uhanov.config.TestConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Profile("test")
@ComponentScan("org.uhanov")
public class WebAppInitializerTestConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();

        rootContext.register(TestConfig.class);

        container.addListener(new ContextLoaderListener(rootContext));


        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(DispatcherConfigTest.class);

        dispatcherContext.getEnvironment()
                .getSystemProperties()
                .put(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");

        ServletRegistration.Dynamic dispatcher =
                container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }
}
