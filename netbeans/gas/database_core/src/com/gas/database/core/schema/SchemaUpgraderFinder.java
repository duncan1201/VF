/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.schema;

import com.gas.database.core.schema.api.ISchemaUpgrader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class SchemaUpgraderFinder {

    final static List<ISchemaUpgrader> all = new ArrayList<ISchemaUpgrader>();

    static {
        for (ISchemaUpgrader u : Lookup.getDefault().lookupAll(ISchemaUpgrader.class)) {
            all.add(u);
        }
        Collections.sort(all, new SchemaUpgraderComparator());
    }
    
    public static List<ISchemaUpgrader> getAll(){
        return all;
    }
    
    public static Integer getMaxVersion(){
        Integer ret = null;
        for (ISchemaUpgrader u : all) {
            if(ret == null){
                ret = u.getToVersion();
            }else{
                if(ret < u.getToVersion()){
                    ret = u.getToVersion();
                }
            }
        }
        return ret;
    }

    public static List<ISchemaUpgrader> getSchemaUpgraders(Integer from) {
        List<ISchemaUpgrader> ret = new ArrayList<ISchemaUpgrader>();
        for (ISchemaUpgrader u : all) {
            if (u.getFromVersion() >= from) {
                ret.add(u);
            }
        }
        return ret;
    }

    private static class SchemaUpgraderComparator implements Comparator<ISchemaUpgrader> {

        @Override
        public int compare(ISchemaUpgrader o1, ISchemaUpgrader o2) {
            Integer from1 = o1.getFromVersion();
            Integer from2 = o2.getFromVersion();
            if (from1 == null && from2 == null) {
                return 0;
            } else if (from1 == null) {
                return -1;
            } else if (from2 == null) {
                return 1;
            } else {
                return from1.compareTo(from2);
            }
        }
    }
}
