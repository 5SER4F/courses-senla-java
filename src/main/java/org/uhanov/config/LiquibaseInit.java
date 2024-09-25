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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class LiquibaseInit {
    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;
    @Value("${db.changeLogFile}")
    private String changeLogs;

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
                        createNewConnection()
                ));
    }

    private Connection createNewConnection() {
        try {
            return DriverManager.getConnection(
                    url,
                    username,
                    password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }
}
