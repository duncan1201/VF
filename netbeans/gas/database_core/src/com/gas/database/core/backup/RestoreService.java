/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.backup;

import com.gas.database.core.backup.api.IRestoreService;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.conn.api.IDbConnSettingsService;
import java.io.File;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IRestoreService.class)
public class RestoreService implements IRestoreService{

    @Override
    public void restore(File file) {      
        IDbConnSettingsService service = Lookup.getDefault().lookup(IDbConnSettingsService.class);
        DbConnSettings settings = service.getDbConnSettings();
        String sql = String.format("RUNSCRIPT from '%s'", file.toString());
        SQLHelper.execute(settings, sql);
    }
    
}
