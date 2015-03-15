package com.gas.database.core.conn.api;

import com.gas.database.core.backup.api.IBackupService;
import java.io.File;
import java.util.Properties;
import org.h2.constant.SysProperties;

public class DbConnSettings {

    public static final String KEY_CONN_URL = "hibernate.connection.url";   
    public static final String KEY_DRIVER_CLASS = "hibernate.connection.driver_class";
    public static final String KEY_SESSION_CONTEXT_CLASS = "hibernate.current_session_context_class";
    public static final String KEY_USERNAME = "hibernate.connection.username";
    public static final String KEY_PWD = "hibernate.connection.password";
    public static final String KEY_BASEDIR = "h2.baseDir";
    
    public static final String JDBC_H2 = "jdbc:h2:tcp://localhost/";
    
    public static final String[] KEYS = {KEY_CONN_URL, KEY_DRIVER_CLASS, KEY_SESSION_CONTEXT_CLASS, KEY_USERNAME, KEY_PWD, KEY_BASEDIR};
    
    private Properties properties;

    public DbConnSettings() {
    }
    
    public DbConnSettings(Properties properties) {
        this.properties = properties;
    }
    
    public File getBaseDir() {        
        File ret = new File(properties.getProperty(KEY_BASEDIR));
        return ret;
    }
    
    public File getDatabaseFileDir(){        
        File dbFile = getDatabaseFile();
        File ret = dbFile.getParentFile();       
        return ret;
    }
    
    public void setDatabaseFile(File file){
        File baseDir = file.getParentFile();
        properties.put(KEY_BASEDIR, baseDir.getAbsolutePath());
                
        String fileName = file.getName();
        String path = fileName.substring(0, fileName.length() - IBackupService.H2DB_EXT.length());
        String url = DbConnSettings.JDBC_H2 + path;  
        properties.put(KEY_CONN_URL, url);
    }
    
    public boolean isDatabaseFilePresent(){
        File file = getDatabaseFile();
        return file.exists();
    }
    
    public File getDatabaseFile(){
    
        StringBuilder ret = new StringBuilder();
        String connURL = properties.getProperty(KEY_CONN_URL);        
        String path = connURL.substring(DbConnSettings.JDBC_H2.length());
        if(path.indexOf(";") > -1){
            path = path.substring(0, path.indexOf(";"));
        }
        File dbFile = null;
        if(path.startsWith("~")){            
            dbFile = new File(new File(System.getProperty("user.home")), path.substring(2) + IBackupService.H2DB_EXT);
        }else{
            String baseDir = properties.getProperty(KEY_BASEDIR);
            dbFile = new File(baseDir, path + IBackupService.H2DB_EXT);
        }
        
        return dbFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
