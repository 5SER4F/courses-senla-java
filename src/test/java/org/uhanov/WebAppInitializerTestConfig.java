package org.uhanov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.uhanov.config.DispatcherConfigTest;
import org.uhanov.config.SecurityTestConfig;
import org.uhanov.config.TestConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Profile("test")
@ComponentScan("org.uhanov")
public class WebAppInitializerTestConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
//    @Override
//    public void onStartup(ServletContext container) throws ServletException {
////        AbstractAnnotationConfigDispatcherServletInitializer
//        AbstractSecurityWebApplicationInitializer
//        AnnotationConfigWebApplicationContext rootContext =
//                new AnnotationConfigWebApplicationContext();
//
//        rootContext.register(TestConfig.class, SecurityTestConfig.class);
//
//        container.addListener(new ContextLoaderListener(rootContext));
//
//
//        AnnotationConfigWebApplicationContext dispatcherContext =
//                new AnnotationConfigWebApplicationContext();
//        dispatcherContext.register(DispatcherConfigTest.class);
//
//        dispatcherContext.getEnvironment()
//                .getSystemProperties()
//                .put(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
//
//        ServletRegistration.Dynamic dispatcher =
//                container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//
//
//    }


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {TestConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DispatcherConfigTest.class, SecurityTestConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }
}
