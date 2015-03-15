package com.gas.database.core.schema.api;

import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author dq
 */
public interface IDatabaseSchemaUpgradeService {
    public static final String TALLE_AS = "AnnotatedSeq";
    public static final String TABLE_VF_PARAM = "vf_parameter";
    public static final String TABLE_OLIGOELEMENT = "OLIGOELEMENT";
    
    boolean isVfParametersTablePresent();
    boolean isSchemaVersionPresent();
    List<ISchemaUpgrader> getISchemaUpgraders();
    boolean upgradeDatabaseSchema(List<ISchemaUpgrader> upgraders);
    Integer getMaxVersion();
}
