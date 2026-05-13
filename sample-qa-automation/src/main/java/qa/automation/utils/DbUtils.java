package qa.automation.utils;

import qa.automation.config.Config;

import java.sql.*;
import java.util.*;

public class DbUtils {

    public static List<Map<String, String>> getGrid(String query, Object... params) {
        List<Map<String, String>> grid = new ArrayList<>();
        String url = Config.oracleDbUrl();

        try (Connection conn = DriverManager.getConnection(url, Config.oracleDbUser(), Config.oracleDbPassword());
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, String> row = new LinkedHashMap<>();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i).toUpperCase();
                        String value = rs.getString(i);
                        row.put(columnName, value != null ? value : "");
                    }
                    grid.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        return grid;
    }
}
