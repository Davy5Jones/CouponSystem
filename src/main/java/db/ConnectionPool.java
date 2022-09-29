package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Created by kobis on 06 Sep, 2022
 */
public class ConnectionPool {

    private static final String url = "jdbc:mysql://localhost:3306?createDatabaseIfNotExist=TRUE&useTimezone=TRUE&serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "1234";

    private static final int NUM_OF_CONS = 10;
    private static final ConnectionPool instance = new ConnectionPool();
    private final Stack<Connection> connections = new Stack<>();

    private ConnectionPool() {

        for (int i = 0; i < NUM_OF_CONS; i++) {
            try {
                connections.push(DriverManager.getConnection(url, username, password));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        synchronized (connections) {
            if (connections.size() == 0) {
                try {
                    connections.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return connections.pop();
    }

    public void restoreConnection(Connection conn) {

        synchronized (connections) {
            connections.push(conn);
            connections.notify();
        }

    }


    public void closeAll() throws SQLException {

        synchronized (connections) {

            while (connections.size() != NUM_OF_CONS) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (Connection conn : connections) {
                conn.close();
            }
        }
    }

    public static ConnectionPool getInstance() {
        return instance;
    }
}
