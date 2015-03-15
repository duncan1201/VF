package com.gas.database.core.service;

import com.gas.database.core.HibernateUtil;
import com.gas.database.core.conn.DbConnSettingsService;
import com.gas.database.core.service.api.IHibernateConfigService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.cfg.Configuration;
import com.gas.database.core.conn.api.DbConnSettings;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=IHibernateConfigService.class)
public class HibernateConfigService implements IHibernateConfigService {

    private static HibernateConfigService instance = new HibernateConfigService();
    private static Configuration defaultConfig;
    private Map<String, Configuration> configMaps;

    public HibernateConfigService() {
    }

    public static HibernateConfigService getInstance() {
        return instance;
    }

   @Override
    public Configuration getDefaultConfiguration() {
        if (defaultConfig == null) {
            Configuration ret = new Configuration();

            /* Database connection settings */
            
            DbConnSettings connSettings = DbConnSettingsService.getInstance().getDbConnSettings();
            ret.setProperties(connSettings.getProperties());

            // BioSql

            Iterator<InputStream> inputStreams = getMappingFilesAsInputStream().iterator();

            while (inputStreams.hasNext()) {
                InputStream inputStream = inputStreams.next();
                if (inputStream != null) {
                    ret.addInputStream(inputStream);
                }

            }

            defaultConfig = ret;
        }
        return defaultConfig;
    }

    private Set<InputStream> getMappingFilesAsInputStream() {
        Set<InputStream> ret = new HashSet<InputStream>();
        
        ret.add(HibernateUtil.class.getResourceAsStream("VfParameter.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("ace/Ace.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("as/AnnotatedSequence.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("as/GC.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("as/SC.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("as/ORF.hbm.xml"));        
        ret.add(HibernateUtil.class.getResourceAsStream("as/TranslationResult.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("as/RMap.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("filesystem/Folder.hbm.xml"));
        
        ret.add(HibernateUtil.class.getResourceAsStream("msa/MSA.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("msa/CLUSTALW_PARAM.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("msa/MUSCLE_PARAM.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("msa/VF_MSA_PARAM.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("pubmed/Pubmed.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("primer/UserInput.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("primer/P3Output.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("primer/GbOutput.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("pdb/PDB.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("pdb/PDBFields.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("ren/RenList.hbm.xml"));
        ret.add(HibernateUtil.class.getResourceAsStream("tigr/Tigr.hbm.xml"));
        
        ret.add(HibernateUtil.class.getResourceAsStream("tasm/Tasm.hbm.xml"));
      
        return ret;
    }
}
