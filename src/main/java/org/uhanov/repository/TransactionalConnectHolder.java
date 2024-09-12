package org.uhanov.repository;

public interface TransactionalConnectHolder {

    void openTransaction();

    void commitTransaction();

    void rollbackTransaction();
}
