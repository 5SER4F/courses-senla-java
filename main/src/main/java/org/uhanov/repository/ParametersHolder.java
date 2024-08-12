package org.uhanov.repository;

import org.uhanov.annotation.Component;
import org.uhanov.annotation.Value;

@Component
public class ParametersHolder {
    @Value("my.param.db")
    private String someText;

    public String getSomeText() {
        return someText;
    }
}
