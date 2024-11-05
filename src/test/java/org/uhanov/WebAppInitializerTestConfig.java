package org.uhanov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.uhanov.config.DispatcherConfigTest;
import org.uhanov.config.SecurityTestConfig;
import org.uhanov.config.TestConfig;

@Profile("test")
@ComponentScan("org.uhanov")
public class WebAppInitializerTestConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{TestConfig.class};
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
