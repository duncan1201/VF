package com.gas.database.core;

import com.gas.database.core.conn.api.DbConnSettings;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.cfg.Configuration;

public class DBUtil {

    public static boolean executeSqls(Properties properties, String... sqls) {
        boolean success = false;
        Connection conn = getConnection(properties);
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            conn.commit();
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuitely(conn);
            return success;
        }
    }

    private static Connection getConnection(Properties properties) {
        Connection ret = null;
        if (properties != null) {
            String driverClass = properties.getProperty(DbConnSettings.KEY_DRIVER_CLASS);
            String userName = properties.getProperty(DbConnSettings.KEY_USERNAME);
            String pwd = properties.getProperty(DbConnSettings.KEY_PWD);
            String url = properties.getProperty(DbConnSettings.KEY_CONN_URL);

            try {
                Class.forName(driverClass);
                ret = DriverManager.getConnection(url, userName,
                        pwd);
            } catch (ClassNotFoundException e) {
                System.out.println();
            } catch (SQLException e) {
                System.out.println();
            }
        }
        return ret;
    }

    public static <T> T executeQuery(Properties properties, String query, Class<T> retType) {
        Connection conn = getConnection(properties);
        T ret = null;
        ResultSet resultSet = null;
        try {
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            boolean empty = !resultSet.isBeforeFirst();
            if (!empty) {
                if(resultSet.next()){
                    if (retType.isAssignableFrom(ResultSet.class)) {
                        ret = (T) resultSet;
                    } else if (retType.isAssignableFrom(String.class)) {
                        ret = (T) resultSet.getString(1);
                    } else if (retType.isAssignableFrom(Integer.class)) {
                        ret = (T) Integer.valueOf(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuitely(conn);
        }
        return ret;
    }

    public static boolean isTablePresent(Properties properties, String targetTableName) {
        boolean ret = false;
        Connection conn = null;
        try {
            conn = getConnection(properties);

            DatabaseMetaData md = conn.getMetaData();
            final String[] types = {"TABLE"};
            ResultSet rs = md.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (tableName.equalsIgnoreCase(targetTableName)) {
                    ret = true;
                    break;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            closeQuitely(conn);
        }
        return ret;
    }

    private static void closeQuitely(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }
}
