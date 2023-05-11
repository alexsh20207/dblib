package org.example;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DataBaseHandler {
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

    public ResultSet getReaders() {
        String getReaders = "SELECT * FROM readers";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getDBConnection().prepareStatement(getReaders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public void insertReader(Reader reader) {
        String insertReader = "INSERT INTO " + reader.getType() + "VALUES";
    }
}
