/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.schema;

import com.gas.database.core.schema.api.ISchemaUpgrader;
import com.gas.database.core.misc.api.VF_PARAMS;
import com.gas.database.core.schema.api.IDatabaseSchemaUpgradeService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service=ISchemaUpgrader.class)
public class SchemaUpgrader implements ISchemaUpgrader {
    
    @Override
    public Integer getFromVersion(){
        return 0;
    }
    
    @Override
    public Integer getToVersion(){
        return 1;
    }    
    
    @Override
    public String[] getSqls(){
        final String dropTableSql = String.format("DROP TABLE IF EXISTS %s", IDatabaseSchemaUpgradeService.TABLE_VF_PARAM);
        final String createTableSql = String.format("CREATE TABLE %s (hibernateId VARCHAR(255) PRIMARY KEY, NAME VARCHAR(255) NOT NULL UNIQUE, VALUE VARCHAR(255) NOT NULL)", IDatabaseSchemaUpgradeService.TABLE_VF_PARAM);
        final String insertSql = String.format("INSERT INTO %s VALUES(1, '%s', '%d');", 
                IDatabaseSchemaUpgradeService.TABLE_VF_PARAM, VF_PARAMS.Database_version, getToVersion());
        final String addColumnOligoElementSql = String.format("ALTER TABLE %s ADD SeqTemplate VARCHAR(255)", IDatabaseSchemaUpgradeService.TABLE_OLIGOELEMENT);
        final String updateSeqTemplateSql = String.format("update %s set seqTemplate = seq;", IDatabaseSchemaUpgradeService.TABLE_OLIGOELEMENT);
        String [] ret = {dropTableSql, createTableSql, insertSql, addColumnOligoElementSql, updateSeqTemplateSql};
        return ret;
    }
}
