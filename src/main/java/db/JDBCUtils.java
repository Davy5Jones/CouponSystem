package db;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Created by kobis on 06 Sep, 2022
 */
public class JDBCUtils {

    public static void runQuery(String sql) throws SQLException {

        // Step 2 - getConnection from CP
        Connection connection = ConnectionPool.getInstance().getConnection();

        // Step 3 - Prepare Statement & Execute
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();

        // Step 5 - restoreConnection to CP
        ConnectionPool.getInstance().restoreConnection(connection);

    }

    public static void runQuery(String sql, Map<Integer, Object> map) throws SQLException {

        // Step 2 - getConnection from CP
        Connection connection = ConnectionPool.getInstance().getConnection();

        // Step 3 - Prepare Statement & Execute
        PreparedStatement statement = connection.prepareStatement(sql);


        statement.execute();

        // Step 5 - restoreConnection to CP
        ConnectionPool.getInstance().restoreConnection(connection);

    }

    public static List<?> runQueryWithResult(String sql) throws SQLException {
        List<?> rows;
        // Step 2 - getConnection from CP
        Connection connection = ConnectionPool.getInstance().getConnection();

        // Step 3 - Prepare Statement & Execute
        PreparedStatement statement = connection.prepareStatement(sql);

        // Step 4 - Support ResultSet
        ResultSet resultSet = statement.executeQuery();
        rows = ConvertUtils.toList(resultSet);

        // Step 5 - restoreConnection to CP & Close resources
        resultSet.close();
        ConnectionPool.getInstance().restoreConnection(connection);

        return rows;
    }

    public static List<?> runQueryWithResult(String sql, Map<Integer, Object> params) throws SQLException {
        List<?> rows;
        // Step 2 - getConnection from CP
        Connection connection = ConnectionPool.getInstance().getConnection();

        // Step 3 - Prepare Statement & Execute
        PreparedStatement statement = connection.prepareStatement(sql);
        supportParams(statement, params);

        // Step 4 - Support ResultSet
        ResultSet resultSet = statement.executeQuery();
        rows = ConvertUtils.toList(resultSet);

        // Step 5 - restoreConnection to CP & Close resources
        resultSet.close();
        ConnectionPool.getInstance().restoreConnection(connection);

        return rows;
    }

    private static PreparedStatement supportParams(PreparedStatement statement, Map<Integer, Object> params) throws SQLException {
        for (Map.Entry<Integer, Object> entry : params.entrySet()) {
            int key = entry.getKey();

            Object value = entry.getValue();
            if (value instanceof Integer) {
                statement.setInt(key, (Integer) value);
                continue;
            }
            if (value instanceof String) {
                statement.setString(key, (String) value);
                continue;
            }
            if (value instanceof Double) {
                statement.setDouble(key, (Double) value);
                continue;
            }
            if (value instanceof Boolean) {
                statement.setBoolean(key, (Boolean) value);
                continue;
            }
            if (value instanceof Date) {
                statement.setDate(key, (Date) value);
            }

        }
        return statement;
    }
}
