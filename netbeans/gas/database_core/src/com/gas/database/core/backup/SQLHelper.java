/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup;

import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class SQLHelper {

    public static void execute(DbConnSettings settings, String... sqls) {

        String driverClass = (String) settings.getProperties().get(DbConnSettings.KEY_DRIVER_CLASS);
        String url = (String) settings.getProperties().get(DbConnSettings.KEY_CONN_URL);
        String username = (String) settings.getProperties().get(DbConnSettings.KEY_USERNAME);
        try {
            Class.forName(driverClass);
            Connection conn = DriverManager.getConnection(url, username, "");
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            for (String sql : sqls) {
                stmt.execute(sql);
            }

            conn.commit();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BackupService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
