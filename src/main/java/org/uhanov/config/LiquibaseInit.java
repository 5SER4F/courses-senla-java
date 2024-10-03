package org.uhanov.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.uhanov.exception.DbConnectionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class LiquibaseInit {
    private final DataSource dataSource;
    private final String changeLogs;


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
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }
}
