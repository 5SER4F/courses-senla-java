package org.uhanov.repository;

import org.uhanov.annotation.Autowire;
import org.uhanov.annotation.Component;

@Component
public class Database implements DatabaseInterface {
    @Autowire
    private ParametersHolder parametersHolder;

    @Override
    public Object execute() {
        return parametersHolder.getSomeText();
    }
}
