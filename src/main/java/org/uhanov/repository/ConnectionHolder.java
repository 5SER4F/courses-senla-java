package org.uhanov.repository;

import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.uhanov.exception.DbConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ConnectionHolder implements SimpleConnectionHolder, TransactionalConnectHolder, DisposableBean {
    private final String url;
    private final String username;
    private final String password;

    private final ConcurrentHashMap<Long, Connection> connectionsWithoutTransaction = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Connection> connectionsWithTransaction = new ConcurrentHashMap<>();

    @Override
    public void destroy() {
        connectionsWithoutTransaction.values().forEach(this::closeConnection);
        connectionsWithTransaction.values().forEach(this::closeConnection);
        connectionsWithoutTransaction.clear();
        connectionsWithTransaction.clear();
    }

    @Override
    public void rollbackTransaction() {
        long currentThreadId = Thread.currentThread().getId();
        Connection connection = connectionsWithTransaction.remove(currentThreadId);
        if (connection == null) {
            throw new DbConnectionException("No transaction in Thread id=" + currentThreadId);
        }
        throwIfClosed(connection);
        try {
            System.out.println("Rollback transaction in Thread id=" + currentThreadId);
            connection.rollback();
            connection.close();
            connectionsWithTransaction.remove(currentThreadId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        long currentThreadId = Thread.currentThread().getId();
        Connection connection;
        if (connectionsWithoutTransaction.containsKey(currentThreadId) &&
                connectionsWithTransaction.containsKey(currentThreadId)) {
            throw new DbConnectionException("To many connections");
        }
        if (connectionsWithTransaction.containsKey(currentThreadId)) {
            return connectionsWithTransaction.get(currentThreadId);
        }

        if (connectionsWithoutTransaction.containsKey(currentThreadId)) {
            connection = connectionsWithoutTransaction.get(currentThreadId);
            try {
                if (connection.isClosed()) {
                    connectionsWithoutTransaction.remove(currentThreadId);
                    return createNewConnection();
                }
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
        connection = createNewConnection();
        connectionsWithoutTransaction.put(currentThreadId, connection);
        return connection;
    }

    @Override
    public void openTransaction() {
        long currentThreadId = Thread.currentThread().getId();
        Connection connection;
        if (connectionsWithTransaction.containsKey(currentThreadId)) {
            throwIfClosed(connectionsWithTransaction.get(currentThreadId));
            return;
        }
        if (connectionsWithoutTransaction.containsKey(currentThreadId)) {
            connection = connectionsWithoutTransaction.remove(currentThreadId);
            connectionsWithTransaction.put(currentThreadId, connection);
            setAutoCommit(connection, false, currentThreadId);
            return;
        }
        connection = createNewConnection();
        setAutoCommit(connection, false, currentThreadId);
    }

    @Override
    public void commitTransaction() {
        long currentThreadId = Thread.currentThread().getId();
        if (!connectionsWithTransaction.containsKey(currentThreadId)) {
            throw new DbConnectionException("Trying close not exist connection int threadId=" + currentThreadId);
        }
        Connection connection = connectionsWithTransaction.remove(currentThreadId);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
        connectionsWithoutTransaction.put(currentThreadId, setAutoCommit(connection, true, currentThreadId));
    }

    private void throwIfClosed(Connection connection) {
        long currentThreadId = Thread.currentThread().getId();
        try {
            if (connection.isClosed()) {
                throw new DbConnectionException("Connection closed in open transaction=" + connection +
                        " in threadId=" + currentThreadId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }

    private Connection setAutoCommit(Connection connection, boolean flag, long currentThread) {
        try {
            connection.setAutoCommit(flag);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            connectionsWithTransaction.remove(currentThread);
            connectionsWithoutTransaction.remove(currentThread);
            closeConnection(connection);
            throw new DbConnectionException();
        }
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

    private void closeConnection(Connection connection) {
        try {
            System.out.println("Closing connection=" + connection);
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed To close" + connection);
            e.printStackTrace();
        }
    }
}
