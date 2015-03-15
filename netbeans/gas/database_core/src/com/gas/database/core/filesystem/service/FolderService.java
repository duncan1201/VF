package com.gas.database.core.filesystem.service;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.DBUtil;
import com.gas.database.core.HibernateUtil;
import com.gas.database.core.api.IDomainUtil;
import com.gas.database.core.as.service.AnnotatedSeqDAO;
import com.gas.database.core.filesystem.service.api.IFolderService;
import java.io.File;

import org.hibernate.cfg.Configuration;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.database.core.filesystem.FolderDAO;
import com.gas.database.core.msa.service.api.IMSAService;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.ren.IRENListService;
import com.gas.database.core.service.api.IDefaultDatabaseService;
import com.gas.database.core.service.api.IHibernateConfigService;
import com.gas.domain.core.FileImportServiceFinder;
import com.gas.domain.core.IFileImportService;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.api.IFetureKeyService;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.nexus.api.Nexus;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pdb.util.PDBParser;
import com.gas.domain.core.primer3.IUserInputFactory;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.util.INCBIPubmedArticleSetParser;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.core.tigr.util.TigrProjectIO;
import com.gas.domain.core.primer3.UserInput;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.util.KromatogramParser;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import org.hibernate.classic.Session;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IFolderService.class)
public class FolderService implements IFolderService {

    private static IHibernateConfigService hibernateConfigService = Lookup.getDefault().lookup(IHibernateConfigService.class);
    private static IDefaultDatabaseService defaultDatabaseService = Lookup.getDefault().lookup(IDefaultDatabaseService.class);
    private static IRENListService renListService = Lookup.getDefault().lookup(IRENListService.class);
    private Configuration config;    

    public FolderService() {
        config = hibernateConfigService.getDefaultConfiguration();
    }

    public FolderService(Configuration config) {
        this.config = config;
    }

    @Override
    public void setConfig(Configuration config) {
        this.config = config;
    }

    @Override
    public void deleteData(Folder folder, IFolderElement... list) {
        FolderDAO daoFolder = new FolderDAO(config);
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        daoFolder.deleteElements(folder, session, false, list);
        AnnotatedSeqDAO daoAs = new AnnotatedSeqDAO(config);
        StringList paths = new StringList();
        for (IFolderElement fe : list) {
            if (fe instanceof AnnotatedSeq) {
                AnnotatedSeq seq = (AnnotatedSeq) fe;
                paths.add(String.format("%s\\%s", folder.getAbsolutePath(), seq.getName()));
            }
        }
        daoAs.deactiveAsPath(paths, session, true);
    }

    @Override
    public Folder loadWithData(String absolutePath) {
        FolderDAO dao = new FolderDAO(config);
        return dao.getFolder(absolutePath, true);
    }

    @Override
    public Folder initFolderTreeIfNecessary() {
        validate();
        return initFolderTreeIfNecessary(config);
    }

    @Override
    public Folder loadWithParents(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, false, false, true);
    }

    @Override
    public Folder loadWithChildren(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, false, false, false);
    }

    @Override
    public Folder loadWithDataAndChildren(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, true, false, false);
    }

    @Override
    public Folder loadWithData(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, true, false, false);
    }

    @Override
    public Folder loadWithDataAndParents(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, true, false, true);
    }

    @Override
    public Folder loadWithDataAndParentAndChildren(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, true, false, true);
    }

    @Override
    public void create(Folder folder) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        dao.persist(folder);
    }
    
    @Override
    public void createNCBIChildFolderIfNeeded(String folderName){
        Folder root = getFolderTree(false);
        Folder ncbiRoot = root.getChild(FolderNames.NCBI_ROOT);
        if(!ncbiRoot.hasChild(folderName)){
            Folder newFolder = new Folder(folderName);
            newFolder.setNCBIFolder(true);
            newFolder.setDeletable(false);                                      
            Folder ncbiRootReloaded = loadWithChildren(ncbiRoot.getHibernateId());
            ncbiRootReloaded.addFolder(newFolder);
            newFolder.setParent(ncbiRootReloaded);            
            merge(ncbiRootReloaded, true);
        }
    }

    private Folder initFolderTreeIfNecessary(Configuration config) {
        Folder ret = null;
        try {
            this.config = config;
            FolderDAO dao = new FolderDAO(config);

            Folder folderTree = dao.getFolderTree();
            if (folderTree == null) {
                folderTree = FolderHelper.getDefaultRootFolderTree();
                create(folderTree);
            }
            ret = dao.getFolderTree();
        } catch (Exception e) {
        }
        return ret;
    }

    @Override
    public Folder getFolderTree() {        
        return getFolderTree(true);
    }
    
    private Folder getFolderTree(boolean includeData){
        validate();
        FolderDAO dao = new FolderDAO(config);
        Folder ret = dao.getFolderTree(includeData);        
        return ret;
    }

    @Override
    public void merge(Folder folder) {      
        merge(folder, false);
    }
    
    public void merge(Folder folder, boolean ncbiFolderAllowed){
        validate();

        FolderDAO dao = new FolderDAO(config);

        dao.merge(folder, ncbiFolderAllowed);
    }

    private void validate() {
    }

    @Override
    public void importInitialData() {
        validate();
        initFolderTreeIfNecessary();

        // init primer3 settings
        importInitialPrimer3Settings();

        File dataDir = InstalledFileLocator.getDefault().locate("modules/ext/exampleData", "com.gas.database.core", false);

        Folder myDataFolderTree = getFolderTree().getFolder(FolderNames.MY_DATA);

        Folder sampleDataFolder = myDataFolderTree.getFolder(FolderNames.SAMPLE_DATA);
        Folder abstractFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_ABSTRACTS_FOLDER);
        Folder nucletidesFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_NUCLEOTIDES_FOLDER);
        Folder enzymesFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_ENZYMES_FOLDER);
        Folder proteinFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_PROTEINS_FOLDER);
        Folder nebFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_NEB_FOLDER);
        Folder structureFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_STRUCTURES_FOLDER);
        Folder shortgunFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_SHORTGUN_FOLDER);
        Folder alignmentsFolder = sampleDataFolder.getFolder(FolderNames.DEFAULT_ALIGNMENTS_FOLDER);

        File[] children = dataDir.listFiles();

        for (File file : children) {
            if (file.isDirectory() && file.getName().equalsIgnoreCase("nucleotide")) {
                importInitialAnnotatedSeq(nucletidesFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase("enzyme")) {
                importInitialEnzymeList(enzymesFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase("protein")) {
                importInitialAnnotatedSeq(proteinFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase("feature_keys")) {
                importInitialFeatureKeys(file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase("abstract")) {
                importInitialAbstracts(abstractFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase(FolderNames.DEFAULT_STRUCTURES_FOLDER)) {
                importInitial3DMolecules(structureFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase(FolderNames.DEFAULT_SHORTGUN_FOLDER)) {
                importInitialShortgunAssemblies(shortgunFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase(FolderNames.DEFAULT_ALIGNMENTS_FOLDER)) {
                importInitialAlignments(alignmentsFolder, file);
            } else if (file.isDirectory() && file.getName().equalsIgnoreCase(FolderNames.DEFAULT_NEB_FOLDER)) {
                importInitialNEBPlasmids(nebFolder, file);
            }
        }
    }

    private void importInitialAlignments(Folder folder, File dir) {
        IFileImportService service;
        File[] fos = dir.listFiles();
        IMSAService msaService = Lookup.getDefault().lookup(IMSAService.class);
        IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);
        for (File file : fos) {
            String name = file.getName();
            String ext = name.substring(name.lastIndexOf(".") + 1);
            service = FileImportServiceFinder.findByExtension(ext);
            if (service != null) {
                Object obj = service.receive(file);
                MSA msa = null;
                if (obj instanceof MSA) {
                    msa = (MSA) obj;
                } else if (obj instanceof Nexus) {
                    msa = msaService.toMSA((Nexus) obj);
                } else {
                    throw new UnsupportedOperationException(String.format("Class %s not supported", obj.getClass().getName()));
                }
                msa.setType("DNA");
                msa.setFolder(folder);

                domainUtil.persist(msa);
            }
        }
    }

    private void importInitialShortgunAssemblies(Folder folder, File dir) {
        File[] fos = dir.listFiles();
        for (File file : fos) {
            String name = file.getName();
            if (name.endsWith(".ser")) {
                TigrProject tp = (TigrProject) TigrProjectIO.read(file);
                if (tp != null) {
                    folder.addObject(tp);
                }
            }else if(name.endsWith(".ab1")){
                Kromatogram kromatogram = KromatogramParser.parse(file);
                String nameK = null;
                int index = name.indexOf('.');
                if(index > -1){
                    nameK = name.substring(0, index);
                }else{
                    nameK = name;
                }
                kromatogram.setRead(false);
                kromatogram.setName(nameK);
                kromatogram.setLastModifiedDate(new Date());
                kromatogram.setDesc("Sample chromatogram");
                folder.addObject(kromatogram);
            }
        }
        merge(folder);
    }

    private void importInitial3DMolecules(Folder folder, File fileObject) {
        PDBParser parser = new PDBParser();

        File[] fos = fileObject.listFiles();
        for (File file : fos) {
            PDBDoc pdb = parser.parse(file);
            folder.addObject(pdb);
        }
        merge(folder);
    }

    private void importInitialAbstracts(Folder folder, File dataFolder) {

        INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);

        File[] files = dataFolder.listFiles();
        for (File file : files) {
            PubmedArticle article = parser.singleParse(file);
            folder.addObject(article);
        }
        merge(folder);
    }

    private void importInitialPrimer3Settings() {
        IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);

        IUserInputFactory userInputFactory = Lookup.getDefault().lookup(IUserInputFactory.class);
        UserInput userInput = userInputFactory.getP3WEB_V_3_0_0();
        userInput.setName("Default Setting");
        userInput.setFavorite(true);
        service.create(userInput);
    }

    private void importInitialFeatureKeys(File dataFolder) {
        IFetureKeyService service = Lookup.getDefault().lookup(IFetureKeyService.class);
        File[] featureKeys = dataFolder.listFiles();

        for (File file : featureKeys) {
            if (file.getName().equalsIgnoreCase("feature_keys.txt") || file.getName().equalsIgnoreCase("feature_keys")) {
                List<FetureKey> fetureKeys = service.getAllInitialFetureKeys(file);
                Iterator<FetureKey> itr = fetureKeys.iterator();
                while (itr.hasNext()) {
                    FetureKey fk = itr.next();
                    service.create(fk);
                }
            }
        }

    }

    private void importInitialNEBPlasmids(Folder folder, File dataFolder) {

        File[] files = dataFolder.listFiles();

        for (File file : files) {

            AnnotatedSeq as = AnnotatedSeqParser.singleParse(file, new FlexGenbankFormat());

            as.setCreationDate(new Date());
            as.setLength(as.getSiquence().getData().length());
            folder.addObject(as);

        }

        merge(folder);
    }

    private void importInitialAnnotatedSeq(Folder folder, File dataFolder) {

        File[] files = dataFolder.listFiles();

        for (File file : files) {
            if (FileHelper.getExt(file).endsWith("gb") || FileHelper.getExt(file).endsWith("gp")) {
                AnnotatedSeq as = AnnotatedSeqParser.singleParse(file, new FlexGenbankFormat());

                as.setCreationDate(new Date());
                as.setLength(as.getSiquence().getData().length());
                folder.addObject(as);
            }
        }

        merge(folder);
    }

    private void importInitialEnzymeList(Folder toFolder, File dataFolder) {

        List<RENList> renLists = renListService.getDefaultEnzymeLists();


        for (RENList renList : renLists) {
            renListService.persist(renList);
        }

        RENList renList = renListService.getMyEnzymeList();
        toFolder.addObject(renList);
        merge(toFolder);
    }

    @Override
    public void delete(Folder folder) {
        validate();
        FolderDAO daoFolder = new FolderDAO(config);
        AnnotatedSeqDAO daoAs = new AnnotatedSeqDAO(config);
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        StringList paths = new StringList();
        Iterator<AnnotatedSeq> itr = folder.getAnnotatedSeqsItr();
        while (itr.hasNext()) {
            AnnotatedSeq as = itr.next();
            paths.add(String.format("%s\\%s", folder.getAbsolutePath(), as.getName()));
        }

        daoFolder.delete(folder, session, false);

        daoAs.deactiveAsPath(paths, session, true);
    }

    @Override
    public Folder loadWithParentAndChildren(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, false, false, true);
    }

    @Override
    public void renameFolder(Folder folder, String newName) {
        if (newName.equals(folder.getName())) {
            return;
        }
        final String oldPath = folder.getAbsolutePath();
        folder.setName(newName);
        final String newPath = folder.getAbsolutePath();

        FolderDAO daoFolder = new FolderDAO(config);
        AnnotatedSeqDAO daoAs = new AnnotatedSeqDAO(config);
        Session session = HibernateUtil.getSessionFactory(config).getCurrentSession();
        daoFolder.merge(folder, session, false);
        daoAs.replaceFolderPath(oldPath, newPath, session, true);
    }

    @Override
    public Folder loadWithRecursiveDataAndParentAndChildren(Integer hId) {
        validate();
        FolderDAO dao = new FolderDAO(config);
        return dao.getByHibernateId(hId, true, true, true);
    }
}
