/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.ren.service;

import com.gas.database.core.HibernateUtil;
import com.gas.database.core.ren.RENListDAO;
import com.gas.database.core.service.api.IDefaultDatabaseService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RENManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dunqiang
 */
@ServiceProvider(service = IRENListService.class)
public class RENListService implements IRENListService {

    private Configuration config;
    static RENList myEnzymeList;
    static RENList defaultAllEnzymeList;
    static RENList defaultAllBluntEndEnzymeList;
    static RENList defaultCommonlyUsedBluntEndEnzymeList;
    static RENList defaultAll5PrimeEnzymeList;
    static RENList defaultCommonlyUsed5PrimeEnzymeList;
    static RENList defaultCommonlyUsedStickyEnzymeList;
    static RENList defaultAll3PrimeEnzymeList;
    static RENList defaultAllStickyEnzymeList;
    static RENList defaultCommonlyUsedOverhang3PrimeEnzymeList;

    static {
        initMyEnzymeList();

        initDefaultAllEnzymeList();

        initDefaultAllBluntEndEnzymeList();
        initDefaultCommonlyUsedBluntEndEnzymeList();

        initDefaultAllStickyEnzymeList();
        initDefaultCommonlyUsedStickyEnzymeList();

        initDefaultAllOverhang5PrimeEnzymeList();
        initDefaultCommonlyUsedOverhang5PrimeEnzymeList();

        initDefaultAllOverhang3PrimeEnzymeList();
        initDefaultCommonlyUsedOverhang3PrimeEnzymeList();
    }

    private static void initMyEnzymeList() {
        myEnzymeList = new RENList();
        myEnzymeList.setDeletable(true);
        
        Set<REN> rens3 = RENManager.getOverhang3PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_MY_3PRIME_STICKY_END);
        Set<REN> rens5 = RENManager.getOverhang5PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_MY_5PRIME_STICKY_END);

        Set<REN> rens = new HashSet<REN>(rens3);
        rens.addAll(rens5);

        myEnzymeList.setName("My cutters");
        myEnzymeList.setRensAndSize(rens);
        myEnzymeList.setCreationDate(new Date());
        myEnzymeList.setLastModifiedDate(new Date());
    }

    @Override
    public RENList getMyEnzymeList() {
        return myEnzymeList;
    }
        
    private static void initDefaultAllEnzymeList() {
        defaultAllEnzymeList = new RENList();
        Set<REN> rens = RENManager.getAllEnzymes(true, 1);
        defaultAllEnzymeList.setName("All cutters");
        defaultAllEnzymeList.setRensAndSize(rens);
        defaultAllEnzymeList.setCreationDate(new Date());
        defaultAllEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultAllBluntEndEnzymeList() {
        defaultAllBluntEndEnzymeList = new RENList();
        Set<REN> rens = RENManager.getBluntEndEnzymes(1);
        defaultAllBluntEndEnzymeList.setName("All blunt-ended cutters");
        defaultAllBluntEndEnzymeList.setRensAndSize(rens);
        defaultAllBluntEndEnzymeList.setCreationDate(new Date());
        defaultAllBluntEndEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultCommonlyUsedBluntEndEnzymeList() {
        defaultCommonlyUsedBluntEndEnzymeList = new RENList();
        Set<REN> rens = RENManager.getBluntEndEnzymes(RENManager.MIN_SUPPLIERS_4_COMMON_BLUNT_END);
        defaultCommonlyUsedBluntEndEnzymeList.setName("Common blunt-ended cutters");
        defaultCommonlyUsedBluntEndEnzymeList.setRensAndSize(rens);
        defaultCommonlyUsedBluntEndEnzymeList.setCreationDate(new Date());
        defaultCommonlyUsedBluntEndEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultAllStickyEnzymeList() {
        defaultAllStickyEnzymeList = new RENList();
        Set<REN> rens5 = RENManager.getOverhang5PrimeEnzymes(1);
        Set<REN> rens3 = RENManager.getOverhang3PrimeEnzymes(1);

        Set<REN> rens = new HashSet<REN>(rens5);
        rens.addAll(rens3);
        defaultAllStickyEnzymeList.setName("All sticky-ended cutters");
        defaultAllStickyEnzymeList.setRensAndSize(rens);
        defaultAllStickyEnzymeList.setCreationDate(new Date());
        defaultAllStickyEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultAllOverhang5PrimeEnzymeList() {
        defaultAll5PrimeEnzymeList = new RENList();
        Set<REN> rens = RENManager.getOverhang5PrimeEnzymes(1);
        defaultAll5PrimeEnzymeList.setName("All 5' overhang cutters");
        defaultAll5PrimeEnzymeList.setRensAndSize(rens);
        defaultAll5PrimeEnzymeList.setCreationDate(new Date());
        defaultAll5PrimeEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultCommonlyUsedStickyEnzymeList() {
        defaultCommonlyUsedStickyEnzymeList = new RENList();
        Set<REN> rens5 = RENManager.getOverhang5PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_COMMON_5PRIME_STICKY_END);
        Set<REN> rens3 = RENManager.getOverhang5PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_COMMON_3PRIME_STICKY_END);

        Set<REN> rens = new HashSet<REN>(rens5);
        rens.addAll(rens3);

        defaultCommonlyUsedStickyEnzymeList.setName("Common sticky-ended cutters");
        defaultCommonlyUsedStickyEnzymeList.setRensAndSize(rens);
        defaultCommonlyUsedStickyEnzymeList.setCreationDate(new Date());
        defaultCommonlyUsedStickyEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultCommonlyUsedOverhang5PrimeEnzymeList() {
        defaultCommonlyUsed5PrimeEnzymeList = new RENList();
        Set<REN> rens = RENManager.getOverhang5PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_COMMON_5PRIME_STICKY_END);
        defaultCommonlyUsed5PrimeEnzymeList.setName("Common 5' overhang cutters");
        defaultCommonlyUsed5PrimeEnzymeList.setRensAndSize(rens);
        defaultCommonlyUsed5PrimeEnzymeList.setCreationDate(new Date());
        defaultCommonlyUsed5PrimeEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultAllOverhang3PrimeEnzymeList() {
        defaultAll3PrimeEnzymeList = new RENList();
        Set<REN> rens = RENManager.getOverhang3PrimeEnzymes(1);
        defaultAll3PrimeEnzymeList.setName("All 3' overhang cutters");
        defaultAll3PrimeEnzymeList.setRensAndSize(rens);
        defaultAll3PrimeEnzymeList.setCreationDate(new Date());
        defaultAll3PrimeEnzymeList.setLastModifiedDate(new Date());
    }

    private static void initDefaultCommonlyUsedOverhang3PrimeEnzymeList() {
        defaultCommonlyUsedOverhang3PrimeEnzymeList = new RENList();
        Set<REN> rens = RENManager.getOverhang3PrimeEnzymes(RENManager.MIN_SUPPLIERS_4_COMMON_3PRIME_STICKY_END);
        defaultCommonlyUsedOverhang3PrimeEnzymeList.setName(IRENListService.COMMON_3_CUTTERS);
        defaultCommonlyUsedOverhang3PrimeEnzymeList.setRensAndSize(rens);
        defaultCommonlyUsedOverhang3PrimeEnzymeList.setCreationDate(new Date());
        defaultCommonlyUsedOverhang3PrimeEnzymeList.setLastModifiedDate(new Date());
    }

    public RENListService() {
        IHibernateConfigService hService = Lookup.getDefault().lookup(IHibernateConfigService.class);
        config = hService.getDefaultConfiguration();
    }

    @Override
    public RENList getDefaultAllEnzymeList() {
        return defaultAllEnzymeList;
    }

    public RENList getDefaultAllBluntEndEnzymeList() {
        return defaultAllBluntEndEnzymeList;
    }

    public RENList getDefaultCommonlyUsedBluntEndEnzymeList() {
        return defaultCommonlyUsedBluntEndEnzymeList;
    }

    public RENList getDefaultAll5PrimeEnzymeList() {
        return defaultAll5PrimeEnzymeList;
    }

    public RENList getDefaultCommonlyUsed5PrimeEnzymeList() {
        return defaultCommonlyUsed5PrimeEnzymeList;
    }

    public RENList getDefaultAll3PrimeEnzymeList() {
        return defaultAll3PrimeEnzymeList;
    }

    public RENList getDefaultCommonlyUsedOverhang3PrimeEnzymeList() {
        return defaultCommonlyUsedOverhang3PrimeEnzymeList;
    }

    public Configuration getConfig() {
        return config;
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public void persist(RENList list) {
        RENListDAO dao = new RENListDAO(config);
        dao.persist(list);
    }

    @Override
    public void deleteRENList(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RENList merge(RENList list) {
        RENListDAO dao = new RENListDAO(config);
        return dao.merge(list);
    }

    @Override
    public List<RENList> getAll() {
        RENListDAO dao = new RENListDAO(config);
        return dao.getRENLists();
    }

    @Override
    public List<String> getAllNames() {
        RENListDAO dao = new RENListDAO(config);
        return dao.getAllNames();
    }

    @Override
    public RENList getRENList(String name) {
        RENList ret = null;
        RENListDAO dao = new RENListDAO(config);
        ret = dao.getRENList(name);
        return ret;
    }

    @Override
    public RENList getByHibernateId(String hId, boolean full) {
        RENList ret = null;
        RENListDAO dao = new RENListDAO(config);
        ret = dao.getByHibernateId(hId, full);
        return ret;
    }

    @Override
    public RENList getFullRENListByHibernateId(String hId) {
        return getByHibernateId(hId, true);
    }

    @Override
    public List<RENList> getAllDeletable(boolean deletable) {
        RENListDAO dao = new RENListDAO(config);
        return dao.getRENLists(deletable);
    }

    public static RENList getDefaultAllStickyEnzymeList() {
        return defaultAllStickyEnzymeList;
    }

    public static RENList getDefaultCommonlyUsedStickyEnzymeList() {
        return defaultCommonlyUsedStickyEnzymeList;
    }

    @Override
    public List<RENList> getDefaultEnzymeLists() {
        List<RENList> ret = new ArrayList<RENList>();

        ret.add(getDefaultAllEnzymeList());

        ret.add(getDefaultAllBluntEndEnzymeList());

        ret.add(getDefaultCommonlyUsedBluntEndEnzymeList());

        ret.add(getDefaultAllStickyEnzymeList());
        ret.add(getDefaultCommonlyUsedStickyEnzymeList());

        ret.add(getDefaultAll5PrimeEnzymeList());

        ret.add(getDefaultCommonlyUsed5PrimeEnzymeList());

        ret.add(getDefaultAll3PrimeEnzymeList());

        ret.add(getDefaultCommonlyUsedOverhang3PrimeEnzymeList());
        return ret;
    }

    @Override
    public int getRENListCount() {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();

        Object obj = session.createQuery("select count(*) from RENList").uniqueResult();
        Integer ret = (Integer) obj;

        session.getTransaction().commit();
        return ret;
    }
}
