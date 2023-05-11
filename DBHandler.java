package org.example;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DBHandler {
    Connection connection;
    ResultSet resultSet;
    public Connection getDBConnection() throws RuntimeException {
        String connectionString = "jdbc:postgresql://localhost:5432/libDB";
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(connectionString, "postgres", "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
    public ResultSet sendRequest(String request) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = getDBConnection().prepareStatement(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (request.startsWith("SELECT")) {
                resultSet = preparedStatement.executeQuery();
            }
            else {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }
}
