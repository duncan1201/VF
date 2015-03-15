/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.conn.api;

/**
 *
 * @author dunqiang
 */
public interface IDbConnSettingsService {

    public static final String DEFAULT_LOCAL_DB_SETTING_NAME = "default_local_db_settings";
        
    boolean promptAndSaveDatabaseFile(boolean forcedRestart);
    boolean isDatabaseConnSet();
    DbConnSettings getDbConnSettings();
    void setDbConnSettings(DbConnSettings settings);
    DbConnSettings getDefaultDbConnSettings();
    boolean isDatabaseConnSetButDbFileNotFound();
    void clearDbConnSettings();
            
}
