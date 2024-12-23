package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    static String url = "jdbc:mysql://localhost:3306/arcade_machine";
    static String username = "admin";
    static String password = "admin";

    public static String currentUser = "";
    public static String currentEmail = "";

    public static void add(String name, String email, String passwordfield) {
        String query = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, passwordfield);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Запись успешно добавлена!");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
    }

    public static boolean checkUser(String email) {
        String query = "SELECT 1 FROM user WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Этот пользователь уже существует");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
        return false;
    }

    public static String getUser(String email) {
        String query = "SELECT username FROM user WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
        return null;
    }

    public static String checkLogIn(String email, String pass) {
        String query = "SELECT username FROM user WHERE email = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, pass);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    public static void updatePassword(String email, String newPassword) {
        String query = "UPDATE user SET password = ? WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setString(2, email);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Пароль успешно обновлён!");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

    public static void changeScore(String email, int score, String game) {
        String query = "UPDATE user SET " + game + " = ? WHERE email = ?";
        String select = "Select " + game + " from user where email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {
            try (PreparedStatement selectStatement = connection.prepareStatement(select)) {
                selectStatement.setString(1, email);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        if (score >= resultSet.getInt(game)) {
                            statement.setInt(1, score);
                            statement.setString(2, email);

                            statement.executeUpdate();
                        } else {
                            System.out.println("Score is lower than current");
                        }
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
    public static int getHighScore(String game, String email) {
        String query = "SELECT " + game + " FROM user WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(game);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return 0;
    }
}
