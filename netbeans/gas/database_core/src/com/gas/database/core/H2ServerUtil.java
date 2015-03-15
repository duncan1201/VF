package com.gas.database.core;

import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.io.File;
import java.sql.SQLException;
import org.h2.tools.Server;
import org.openide.util.Lookup;

public class H2ServerUtil {

    private static Server tcpServer;
    private static IDbConnSettingsService svc = Lookup.getDefault().lookup(IDbConnSettingsService.class);

    private synchronized static Server createTcpServer() {                
        try {
            DbConnSettings settings = svc.getDbConnSettings();
            File baseDir = settings.getBaseDir();
            tcpServer = Server.createTcpServer(
                    new String[]{"-tcpAllowOthers", "-baseDir", baseDir.getAbsolutePath()});
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return tcpServer;
    }

    public synchronized static boolean startTcpServer() {
        Boolean ret = null;
        try {
            String status = getTcpServer().getStatus();
            if (status.indexOf("running") < 0) {
                getTcpServer().start();
            }
            ret = true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public synchronized static void stopTcpServer() {
        String status = getTcpServer().getStatus();
        System.out.println(status);

        if (status.indexOf("running") > -1) {
            getTcpServer().shutdown();
        }
        status = getTcpServer().getStatus();
        System.out.println(status);
    }

    public synchronized static boolean isRunning() {
        String status = getTcpServer().getStatus();
        return (status.indexOf("running") > -1);
    }

    public static Server getTcpServer() {
        if (tcpServer == null) {
            createTcpServer();
        }
        return tcpServer;
    }
}
