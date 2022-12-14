package br.com.alura.ecommerce.database;

import java.sql.*;

public class LocalDatabase {
    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/" + name + ".db";
        connection = DriverManager.getConnection(url);

    }

    public void createIfNoExists(String sql) {
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private PreparedStatement prepare(String statement, String[] params) throws SQLException {
        var preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i + 1, params[i]);
        }
        return preparedStatement;
    }

    public boolean update(String statement, String... params) throws SQLException {
        return prepare(statement, params).execute();
    }

    public ResultSet query(String statement, String... params) throws SQLException {
        return prepare(statement, params).executeQuery();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
