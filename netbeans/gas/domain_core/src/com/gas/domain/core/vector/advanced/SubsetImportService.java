/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.vector.advanced;

import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.vector.advanced.api.ISubsetImportService;
import java.io.*;
import java.util.*;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = ISubsetImportService.class)
public class SubsetImportService implements ISubsetImportService {

    public static final String NUCLEOTIDE_LIST_FILE = "MSList.vdb";
    public static final String PROTEIN_LIST_FILE = "PSList.vdb";
    public static final String CITATION_LIST_FILE = "CitationSubbases.vdb";
    public static final String ENZYMES_LIST_FILE = "ESList.vdb";
    public static final String OLIGO_LIST_FILE = "OSList.vdb";
    /*
     * 192 200|protein_subset_04 50
     */
    private final static String START = "192";
    private final static String SUBSET_NAME = "200\\|(.+)";
    private final static String CONTENT_LIST = "201\\|(.+)";
    private final static String END = "50";

    private Map<String, List<String>> receive(InputStream inputStream) {
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            String subsetName = null;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.matches(START)) {
                } else if (line.matches(SUBSET_NAME)) {
                    subsetName = StrUtil.extract(SUBSET_NAME, line);
                    if (!ret.containsKey(subsetName)) {
                        ret.put(subsetName, new ArrayList<String>());
                    }
                } else if (line.matches(CONTENT_LIST)) {
                    String content = StrUtil.extract(CONTENT_LIST, line);
                    String[] splits = content.split("\\|");
                    ret.get(subsetName).addAll(Arrays.asList(splits));
                } else if (line.matches(END)) {
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    private Map<String, List<String>> receive(File file) {
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ret = receive(inputStream);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    @Override
    public Map<String, List<String>> importProteinSubsets(File dbDir) {
        final File dir = new File(dbDir, "Tables" + File.separatorChar + PROTEIN_LIST_FILE);
        return receive(dir);
    }
    
    @Override
    public Map<String, List<String>> importOligoSubsets(File dbDir){
        final File dir = new File(dbDir, "Tables" + File.separatorChar + OLIGO_LIST_FILE);
        return receive(dir);
    }

    @Override
    public Map<String, List<String>> importNucleotideSubsets(File dbDir) {
        final File dir = new File(dbDir, "Tables" + File.separatorChar + NUCLEOTIDE_LIST_FILE);
        return receive(dir);
    }
}
