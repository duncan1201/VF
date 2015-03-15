/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.vector.advanced.api.IImportDBService;
import com.gas.domain.core.vector.advanced.api.IMolImportService;
import com.gas.domain.core.vector.advanced.api.IOligoImportService;
import com.gas.domain.core.vector.advanced.api.ISubsetImportService;
import java.io.File;
import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IImportDBService.class)
public class ImportDbService implements IImportDBService {

    IMolImportService molImportService = Lookup.getDefault().lookup(IMolImportService.class);
    IOligoImportService oligoImportService = Lookup.getDefault().lookup(IOligoImportService.class);
    
    @Override
    public Folder receive(final File dbDir) {
        if (!dbDir.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s must be a directory", dbDir.getName()));
        }        
        Folder ret = new Folder("VNTI Advanced DB");

        // import RNAs/DNAs molecules
        Folder nFolder = new Folder("DNA/RNA Molecules");
        ISubsetImportService subsetImportService = Lookup.getDefault().lookup(ISubsetImportService.class);
        Map<String, List<String>> nSubsets = subsetImportService.importNucleotideSubsets(dbDir);
        ret.addFolder(nFolder);
        List<AnnotatedSeq> nucleotides = molImportService.receiveNucleotidesFromDB(dbDir);
        createSubfolders(nFolder, nSubsets);
        putIntoFolders(nFolder, nucleotides, nSubsets);

        // import protein molecules
        Folder proteinFolder = new Folder("Protein Molecules");
        ret.addFolder(proteinFolder);
        Map<String, List<String>> proteinSubsets = subsetImportService.importProteinSubsets(dbDir);
        List<AnnotatedSeq> proteins = molImportService.receiveProteinsFromDB(dbDir);
        createSubfolders(proteinFolder, proteinSubsets);
        putIntoFolders(proteinFolder, proteins, proteinSubsets);

        // import oligos
        Folder oligoFolder = new Folder("Oligos");
        ret.addFolder(oligoFolder);
        Map<String, List<String>> oligoSubsets = subsetImportService.importOligoSubsets(dbDir);
        List<AnnotatedSeq> oligos = oligoImportService.receiveOligosFromDB(dbDir);
        createSubfolders(oligoFolder, oligoSubsets);
        putIntoFolders(oligoFolder, oligos, oligoSubsets);
        
        return ret;
    }      

    private void createSubfolders(final Folder folder, final Map<String, List<String>> subsets) {
        Iterator<String> itr = subsets.keySet().iterator();
        while (itr.hasNext()) {
            String subset = itr.next();
            folder.addFolder(new Folder(subset));
        }
    }

    private void putIntoFolders(Folder folder, final List<AnnotatedSeq> asList, final Map<String, List<String>> subsets) {
        List<String> added = new ArrayList<String>();

        for (AnnotatedSeq as : asList) {
            List<String> subsetNames = getSubsetNamesForMol(subsets, as.getName());
            for (String sbName : subsetNames) {
                Folder childFolder = folder.getChild(sbName);
                if (childFolder == null) {
                    childFolder = new Folder(sbName);
                    folder.addFolder(childFolder);
                }
                childFolder.addObject(as.clone());
                added.add(as.getName());
            }
        }

        for (AnnotatedSeq as : asList) {
            if (!added.contains(as.getName())) {
                folder.addObject(as);
            }
        }

    }

    private List<String> getSubsetNamesForMol(final Map<String, List<String>> subsets, final String molName) {
        List<String> ret = new ArrayList<String>();
        Iterator<String> keyItr = subsets.keySet().iterator();
        while (keyItr.hasNext()) {
            String subsetName = keyItr.next();
            List<String> molNames = subsets.get(subsetName);
            if (molNames.contains(molName)) {
                ret.add(subsetName);
            }
        }
        return ret;
    }
}
