package org.uhanov.service;

import org.uhanov.annotation.Autowire;
import org.uhanov.annotation.Component;
import org.uhanov.repository.DatabaseInterface;

@Component
public class Service implements ServiceInterface{
    private DatabaseInterface database;

    @Override
    public Object execute() {
        return database.execute();
    }

    @Autowire
    public void setDatabase(DatabaseInterface database) {
        this.database = database;
    }
}
