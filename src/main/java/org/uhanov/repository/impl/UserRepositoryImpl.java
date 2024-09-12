package org.uhanov.repository.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.exception.DbConnectionException;
import org.uhanov.model.User;
import org.uhanov.repository.SimpleConnectionHolder;
import org.uhanov.repository.api.UserRepository;

import java.sql.*;
import java.util.UUID;

@Repository
@Data
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String INSERT_USER = "INSERT INTO \"user\"(\n" +
            " password, balance, firstname, surname, nickname, birth_date, registration_date, country)\n" +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USER_BY_ID = "UPDATE \"user\"\n" +
            "SET balance=?, firstname=?, surname=?, nickname=?, birth_date=?, registration_date=?, country=?\n" +
            "WHERE id=?";

    public static final String GET_USER_BY_ID =
            "SELECT id, password, balance, firstname, surname, nickname, birth_date, registration_date, country\n" +
                    " FROM \"user\"\n" +
                    " WHERE id=?";
    public static final String DELETE_USER_BY_ID = "DELETE FROM \"user\"\n" +
            " WHERE id=?";
    private final SimpleConnectionHolder connectionHolder;

    @Override
    public User get(UUID id) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID)) {
            statement.setObject(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return parseResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }

    @Override
    public User add(User user) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getPassword());
            statement.setDouble(2, user.getBalance());
            statement.setString(3, user.getFirstname());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getNickname());
            statement.setDate(6, Date.valueOf(user.getBirthDate()));
            statement.setTimestamp(7, Timestamp.valueOf(user.getRegistrationDate()));
            statement.setString(8, user.getCountry());
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    user.setId(UUID.fromString(rs.getString("id")));
                }
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }

    }

    @Override
    public void update(User user) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_BY_ID)) {
            statement.setDouble(1, user.getBalance());
            statement.setString(2, user.getFirstname());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getNickname());
            statement.setDate(5, Date.valueOf(user.getBirthDate()));
            statement.setTimestamp(6, Timestamp.valueOf(user.getRegistrationDate()));
            statement.setString(7, user.getCountry());
            statement.setObject(8, user.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }

    }

    @Override
    public void remove(UUID uuid) {
        Connection connection = connectionHolder.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            statement.setObject(1, uuid);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbConnectionException();
        }
    }

    private User parseResultSet(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject("id", UUID.class))
                .password(resultSet.getString("password"))
                .balance(resultSet.getDouble("balance"))
                .firstname(resultSet.getString("firstname"))
                .surname(resultSet.getString("surname"))
                .nickname(resultSet.getString("nickname"))
                .birthDate(resultSet.getDate("birth_date").toLocalDate())
                .registrationDate(resultSet.getTimestamp("registration_date").toLocalDateTime())
                .country(resultSet.getString("country"))
                .build();
    }

}
