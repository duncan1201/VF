/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.schema;

import com.gas.database.core.schema.api.ISchemaUpgrader;
import com.gas.database.core.DBUtil;
import com.gas.database.core.misc.api.VF_PARAMS;
import com.gas.database.core.conn.DbConnSettingsService;
import com.gas.database.core.conn.api.DbConnSettings;
import com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService;
import static com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService.TABLE_VF_PARAM;
import java.util.ArrayList;
import java.util.List;
import org.biojava.bio.search.FilterTest;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=IDatabaseSchemaUpgradeService.class)
public class DatabaseSchemaUpgradeService implements IDatabaseSchemaUpgradeService {        
    
    @Override
    public boolean upgradeDatabaseSchema(List<ISchemaUpgrader> upgraders){
        
        DbConnSettings settings = DbConnSettingsService.getInstance().getDbConnSettings();
        for(ISchemaUpgrader u: upgraders){
            boolean success = DBUtil.executeSqls(settings.getProperties(), u.getSqls());
            if(!success){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public List<ISchemaUpgrader> getISchemaUpgraders(){
        
        List<ISchemaUpgrader> ret;
        
        boolean isParamTablePresent = isVfParametersTablePresent();
        if(!isParamTablePresent){
            ret = SchemaUpgraderFinder.getAll();
        } else{
            Integer currentVersion = getCurrentSchemaVersion();
            ret = SchemaUpgraderFinder.getSchemaUpgraders(currentVersion);
        }
        
        return ret;
    }
    
    @Override
    public boolean isVfParametersTablePresent(){
        DbConnSettings settings = DbConnSettingsService.getInstance().getDbConnSettings();
        boolean tablePresent = DBUtil.isTablePresent(settings.getProperties(), IDatabaseSchemaUpgradeService.TABLE_VF_PARAM);
        return tablePresent;
    }
    
    @Override
    public boolean isSchemaVersionPresent() {
        DbConnSettings settings = DbConnSettingsService.getInstance().getDbConnSettings();
        boolean tablePresent = DBUtil.isTablePresent(settings.getProperties(), IDatabaseSchemaUpgradeService.TABLE_VF_PARAM);
        if(!tablePresent){
            return false;
        }
        final String countSql = "select count(*) from %s where name = '%s'";
        String sql = String.format(countSql, IDatabaseSchemaUpgradeService.TABLE_VF_PARAM, VF_PARAMS.Database_version);
        Integer count = DBUtil.executeQuery(settings.getProperties(), sql, Integer.class);
        
        return count > 0;
    }
    
    public Integer getCurrentSchemaVersion(){
        DbConnSettings settings = DbConnSettingsService.getInstance().getDbConnSettings();
        String sql = String.format("select value from %s where name = '%s'", TABLE_VF_PARAM, "Database_version");
        Integer ret = DBUtil.executeQuery(settings.getProperties(), sql, Integer.class);
        return ret;
    }
    
    public void initTo(){
        
    }

    public Integer getMaxVersion(){
        Integer ret = SchemaUpgraderFinder.getMaxVersion();
        return ret;
    }
}
