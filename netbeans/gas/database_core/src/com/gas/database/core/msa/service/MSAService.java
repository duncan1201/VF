/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.database.core.msa.service;

import com.gas.common.ui.core.IntList;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.matrix.api.IMatrixService;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.matrix.api.MatrixList;
import com.gas.database.core.HibernateUtil;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.IConsensusService;
import com.gas.database.core.msa.service.api.IConservationService;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.misc.api.INewickIOService;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.Apr;
import com.gas.domain.core.msa.ConsensusParam;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.MSAHelper;
import com.gas.domain.core.msa.MSAList;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import com.gas.domain.core.nexus.api.CharactersBlk;
import com.gas.domain.core.nexus.api.Nexus;
import com.gas.domain.core.nexus.api.TreesBlk;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMSAService.class)
public class MSAService implements IMSAService {

    private static IHibernateConfigService hibernateConfigService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private Configuration config;

    public MSAService() {
        config = hibernateConfigService.getDefaultConfiguration();
    }

    @Override
    public MSAList getFull(List<MSA> msas) {
        StringList ids = new StringList();
        for (MSA msa : msas) {
            ids.add(msa.getHibernateId());
        }
        return getByHibernateIds(ids.toArray(new String[ids.size()]));
    }
    
    @Override
    public MSA toMSA(Apr apr){
        MSA ret = toMSA(apr.getData());        
        return ret;
    }
    
    @Override
    public MSA toMSA(Map<String, String> data){
        MSA ret = new MSA();
        ret.setEntries(data);
        ClustalwParam clustalParam = new ClustalwParam();
        clustalParam.getGeneralParam().setType(ret.isDnaByGuess() ? GeneralParam.TYPE.DNA : GeneralParam.TYPE.PROTEIN);
        ret.setClustalwParam(clustalParam);
        ret.setName("Sample Alignment");
        ret.setDesc(String.format("Alignment of %d sequences", ret.getEntries().size()));
        createConsensus(ret);
        createQualityScores(ret);        
        return ret;
    }

    @Override
    public MSA toMSA(Nexus nexus) {
        MSA ret = new MSA();
        if (nexus.containsBlk("characters")) {
            CharactersBlk blk = new CharactersBlk(nexus.getBlk("characters"));
            Map<String, String> matrix = blk.getMatrix();
            ret = toMSA(matrix);           
        }
        if (nexus.containsBlk("trees")) {
            TreesBlk blk = new TreesBlk(nexus.getBlk("trees"));
            Map<String, String> treeStrs = blk.getTreeStrs();
            if (!treeStrs.isEmpty()) {
                INewickIOService newickService = Lookup.getDefault().lookup(INewickIOService.class);
                Newick newick = newickService.parse(treeStrs.values().iterator().next());
                ret.setNewick(newick);
            }
        }
        return ret;
    }

    public Nexus toNexus(MSA msa) {
        return null;
    }

    @Override
    public MSAList getByHibernateIds(String... hIds) {
        StringList intList = new StringList(hIds);
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();

        session.beginTransaction();

        Query query = session.createQuery(String.format("from MSA where hibernateId in(%s)", intList.toString(",", true)));

        MSAList ret = new MSAList(query.list());
        for (MSA msa : ret) {
            MSAHelper.touchAll(msa);
        }

        session.getTransaction().commit();

        return ret;
    }

    @Override
    public MSA merge(MSA msa) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        msa.setLastModifiedDate(new Date());
        MSA ret = (MSA) session.merge(msa);

        session.getTransaction().commit();
        return ret;
    }

    @Override
    public void mergeSetting(MSA msa) {
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        msa.setLastModifiedDate(new Date());
        session.merge(msa.getMsaSetting());

        session.getTransaction().commit();
    }

    @Override
    public void createQualityScores(MSA msa) {
        ICounterListService counterService = Lookup.getDefault().lookup(ICounterListService.class);
        CounterList counterList = counterService.createCounterList(msa.getEntriesMapCopy());

        IConservationService conService = Lookup.getDefault().lookup(IConservationService.class);
        IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
        MatrixList matrixList = matrixService.getAllMatrices();

        Matrix matrix;
        if (msa.getSubMatrix() != null) {
            matrix = matrixList.getMatrix(msa.getSubMatrix());
        } else {
            if (msa.isDNA()) {
                matrix = matrixService.getDefaultDnaMatrix();
            } else {
                matrix = matrixService.getDefaultProteinMatrix();
            }
            msa.setSubMatrix(matrix.getName());
        }
        int[] qualities = conService.calculate(counterList, matrix);
        msa.setQualityScores(qualities);
    }

    @Override
    public void createConsensus(MSA msa) {
        if (msa.getConsensus() == null) {
            IConsensusService consensusService = Lookup.getDefault().lookup(IConsensusService.class);
            if (msa.getConsensusParam() == null) {
                ConsensusParam consensusParams = new ConsensusParam();
                msa.setConsensusParam(consensusParams);
            }
            String consensus = consensusService.calculate(msa.getEntriesMapCopy(), msa.getConsensusParam(), msa.isDnaByGuess());
            msa.setConsensus(consensus);
        }
    }

    @Override
    public void persist(MSA msa) {
        msa.setLastModifiedDate(new Date());
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        session.beginTransaction();
        session.persist(msa);

        session.getTransaction().commit();
    }
}
