package org.uhanov.conroller;

import org.uhanov.annotation.Autowire;
import org.uhanov.annotation.Component;
import org.uhanov.service.ServiceInterface;

@Component
public class Controller {
    private final ServiceInterface service;

    @Autowire
    public Controller(ServiceInterface service) {
        this.service = service;
    }

    public String execute() {
        return service.execute().toString();
    }
}
