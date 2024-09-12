package org.uhanov.repository;

import java.sql.Connection;

public interface SimpleConnectionHolder {

    Connection getConnection();
}
