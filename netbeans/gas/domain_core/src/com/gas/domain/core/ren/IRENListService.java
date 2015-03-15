/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.ren;

import java.util.List;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author dunqiang
 */
public interface IRENListService {
    public static final String COMMON_3_CUTTERS = "Common 3' overhang cutters";

    RENList getMyEnzymeList();
    
    List<RENList> getDefaultEnzymeLists();
    
    RENList getDefaultAllEnzymeList();
    
    int getRENListCount();
 
    // from the database
    void setConfig(Configuration config);

    List<RENList> getAll();

    List<RENList> getAllDeletable(boolean deletable);

    List<String> getAllNames();

    RENList getRENList(String name);

    RENList getFullRENListByHibernateId(String hibernateId);

    RENList getByHibernateId(String hibernateId, boolean full);
    /*
     RENList getAllBluntEndEnzymeList();
     RENList getCommonlyUsedBluntEndEnzymeList();
     RENList getAllOverhang5PrimeEnzymeList();
     RENList getCommonlyUsedOverhang5PrimeEnzymeList();
     RENList getAllOverhang3PrimeEnzymeList();
     RENList getCommonlyUsedOverhang3PrimeEnzymeList();
     */

    // user-defined REN list
    //void createRENList(RENList list);    
    void deleteRENList(String id);

    RENList merge(RENList list);

    void persist(RENList list);
}
