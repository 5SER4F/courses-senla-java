package org.uhanov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.uhanov.repository.ConnectionHolder;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        return configurer;
    }

    @Bean
    public ConnectionHolder connectionHolder() {
        return new ConnectionHolder(System.getenv("db.url"),
                System.getenv("db.username"),
                System.getenv("db.password"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(initMethod = "updateLiquibase")
    public LiquibaseInit liquibase(ConnectionHolder connectionHolder) {
        return new LiquibaseInit(connectionHolder);
    }


}
