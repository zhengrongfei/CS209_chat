package com.example.demo;
import java.sql.*;

public class MySQLConnector {

    public static Connection connection;
    private String url;
    private String username;
    private String password;

    public MySQLConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate(query);
        return rowsAffected;
    }

    // 示例用法
    public static void main(String[] args) {
        // 初始化数据库连接参数
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "mypassword";

        // 创建MySQLConnector实例
        MySQLConnector connector = new MySQLConnector(url, username, password);

        try {
            // 连接数据库
            connector.connect();

            // 执行查询操作
            ResultSet resultSet = connector.executeQuery("SELECT * FROM mytable");
            while (resultSet.next()) {
                String column1 = resultSet.getString("column1");
                int column2 = resultSet.getInt("column2");
                System.out.println("Column1: " + column1 + ", Column2: " + column2);
            }

            // 执行更新操作
            int rowsAffected = connector.executeUpdate("UPDATE mytable SET column1='newvalue' WHERE column2=1");
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 断开数据库连接
            try {
                connector.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
