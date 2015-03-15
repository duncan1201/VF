/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup.api;

import java.io.File;

/**
 *
 * @author dq
 */
public interface IBackupService {
        
    public static final String H2DB_EXT = ".h2.db";
    
    static final String KEY_LAST_BACKUP_TIME = "lastBackupTime";
    
    static final String KEY_BACKUPDIR = "backupDir";
    
    static final String KEY_BACKUP_OPTION = "backupOption";
    
    public static final String EXT = "vfrds";

    void script(File file);
    
    Long getLastBackupTime();
    
    void setLastBackupTime(long time);
    
    void updateBackupDir(File backupDir);
    
    void updateBackupDir(File backupDir, boolean moveBackups);
    
    boolean isMarkForRestoreFromBackupDir();
    
    void restoreFromBackupDir();
    
    File getBackupDir();
    
    String getOriginalFileName(File dbBackupFile);
    
    boolean isBackupDirEmpty();
    
    void markForRestoreFromBackupDir(boolean markForRestoreFromBackupDir, File backupDbFile);
    
    File[] getDbFilesFromBackupDir();
    
    long getDbFilesSizeFromBackupDir();
    
    File getDefaultBackupDir();
    
    BackupOption getBackupOption();
    
    BackupOption getDefaultBackupOption();
    
    void saveBackupOption(BackupOption backupOption);
    
    void backupToBackupDir();
    
    boolean isBackupDirEnoughSpace();
}
