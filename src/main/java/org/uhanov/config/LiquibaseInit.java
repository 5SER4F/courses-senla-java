package org.uhanov.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.uhanov.exception.DbConnectionException;
import org.uhanov.repository.ConnectionHolder;

import java.sql.SQLException;

@Component
public class LiquibaseInit {
    @Value("${db.changeLogFile}")
    private String changeLogs;
    private final ConnectionHolder connectionHolder;

    public LiquibaseInit(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public void updateLiquibase() {
        try (Liquibase liquibase = new Liquibase(changeLogs, new ClassLoaderResourceAccessor(), getDatabase())) {
            liquibase.update("");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }

    private Database getDatabase() throws DatabaseException, SQLException {
        return DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(
                        connectionHolder.getConnection()
                ));
    }
}
