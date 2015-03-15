package com.gas.database.core.conn;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.backup.api.IBackupService;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;

@ServiceProvider(service = IDbConnSettingsService.class)
public class DbConnSettingsService implements IDbConnSettingsService {

    private static DbConnSettingsService instance;

    public DbConnSettingsService() {
    }

    public static DbConnSettingsService getInstance() {
        if (instance == null) {
            instance = new DbConnSettingsService();
        }
        return instance;
    }

    @Override
    public boolean promptAndSaveDatabaseFile(boolean forcedRestart) {
        final Frame frame = WindowManager.getDefault().getMainWindow();
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);

        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String name = f.getName();
                    return name.endsWith(IBackupService.H2DB_EXT);
                }
            }

            @Override
            public String getDescription() {
                return "H2 database file(*.h2.db)";
            }
        });

        UIUtil.showDialog(fc, frame);

        File file = fc.getSelectedFile();

        if (file != null) {

            DbConnSettings dbSettings = getDbConnSettings();
            dbSettings.setDatabaseFile(file);
            setDbConnSettings(dbSettings);
            if (forcedRestart) {
                String msg = "Database file has been changed.";
                String msg2 = "Please restart VectorFriends for the changes to take effect";
                DialogDescriptor.Message m = new DialogDescriptor.Message(String.format(CNST.MSG_FORMAT, msg, msg2), DialogDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(m);

                LifecycleManager mgr = LifecycleManager.getDefault();
                mgr.markForRestart();
                mgr.exit();
            }
        }

        return file != null;
    }

    @Override
    public void setDbConnSettings(DbConnSettings settings) {
        saveDatabaseConnSettings(settings);
    }
    
    public boolean isDatabaseConnSetButDbFileNotFound(){
        boolean isSet = isDatabaseConnSet();
        if(isSet){
            
            return false;
        }else{
            return false;
        }
    }

    @Override
    public DbConnSettings getDbConnSettings() {
        DbConnSettings ret = null;
        boolean isSet = isDatabaseConnSet();
        if (!isSet) {
            return null;
        } else {
            try {
                Preferences prefs = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
                Properties properties = new Properties();
                for (String key : prefs.keys()) {
                    properties.setProperty(key, prefs.get(key, ""));
                }
                ret = new DbConnSettings(properties);
            } catch (BackingStoreException ex) {
                Logger.getLogger(DbConnSettingsService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return ret;
    }

    @Override
    public boolean isDatabaseConnSet() {
        Preferences prefs = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);

        try {
            List keys = Arrays.asList(prefs.keys());
            for (String k : DbConnSettings.KEYS) {
                boolean contains = keys.contains(k);
                if (!contains) {
                    return false;
                }
            }
        } catch (BackingStoreException ex) {
            Logger.getLogger(DbConnSettingsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public DbConnSettings getDefaultDbConnSettings() {
        DbConnSettings ret = new DbConnSettings();

        Properties ppr = new Properties();
        try {
            ppr.load(DbConnSettingsService.class.getResourceAsStream(IDbConnSettingsService.DEFAULT_LOCAL_DB_SETTING_NAME));
            ppr.setProperty(DbConnSettings.KEY_CONN_URL, DbConnSettings.JDBC_H2 + "~/VectorFriends/db_remote/" + getDefaultDbName());

            // 
            File baseDir = new File(System.getProperty("user.home"));
            ppr.setProperty(DbConnSettings.KEY_BASEDIR, baseDir.getAbsolutePath());

            ret.setProperties(ppr);            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    private String getDefaultDbName() {
        return "myDb";
    }
    
    @Override
    public void clearDbConnSettings(){
        Preferences prefs = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
        try {
            prefs.clear();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void saveDatabaseConnSettings(DbConnSettings settings) {
        Preferences prefs = Preferences.userNodeForPackage(DoNotMoveOrDelete.class);
        Properties ppr = settings.getProperties();

        for (String name : ppr.stringPropertyNames()) {
            String value = ppr.getProperty(name);
            prefs.put(name, value);
        }
        try {
            prefs.flush();

        } catch (BackingStoreException ex) {
            Logger.getLogger(DbConnSettingsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
